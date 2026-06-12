import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

admin.initializeApp();
const db = admin.firestore();

/**
 * Interface representing incoming outfit feedback from the Android frontend.
 */
interface WearFeedback {
  id: string;
  outfitId: string;
  items: string[];
  rating: string; // "✨" (Sublime), "🙂" (Balanced), "😕" (Recalibrate)
  score: number;
  timestamp: number;
  weatherTemp: number;
  weatherDesc: string;
}

/**
 * Cloud Function triggered on any write to the 'wearFeedback' collection.
 * Consumes the daily outfit feedback, aggregates all historic styling ratings,
 * recalculates global item and styling affinities, and stores the retrained
 * weights in the '/outfitModel/trained_weights' document.
 */
export const retrainHarmonyModel = functions.firestore
  .document("wearFeedback/{feedbackId}")
  .onWrite(async (change, context) => {
    functions.logger.info("Initializing SARTORIAL AURA Model Retraining Pipeline...", {
      feedbackId: context.params.feedbackId,
    });

    try {
      // 1. Fetch all historic outfit feedback documents from the 'wearFeedback' collection
      const querySnapshot = await db.collection("wearFeedback").get();
      const feedbacks: WearFeedback[] = [];

      querySnapshot.forEach((doc) => {
        const data = doc.data();
        if (data && Array.isArray(data.items)) {
          feedbacks.push({
            id: data.id || doc.id,
            outfitId: data.outfitId || "",
            items: data.items,
            rating: data.rating || "🙂",
            score: Number(data.score) || 0,
            timestamp: Number(data.timestamp) || 0,
            weatherTemp: Number(data.weatherTemp) || 0,
            weatherDesc: data.weatherDesc || "",
          });
        }
      });

      functions.logger.info(`Successfully loaded ${feedbacks.length} styling feedback records.`);

      if (feedbacks.length === 0) {
        functions.logger.info("No feedback entries available. Halting retraining loop.");
        return null;
      }

      // 2. Retraining Core: Compute updated item affinities and pair combinations
      // We analyze individual item weight adjustments based on user sentiment
      const itemScores: { [itemId: string]: number } = {};
      const itemCounts: { [itemId: string]: number } = {};
      
      // Calculate overall rating counts
      let sublimeCount = 0;
      let balancedCount = 0;
      let recalibrateCount = 0;

      for (const fb of feedbacks) {
        // Quantify the emotion feedback:
        // ✨ (Sublime) -> +15 points
        // 🙂 (Balanced) -> +6 points
        // 😕 (Recalibrate) -> -25 points (strong guard rails against uncomfortable/unharmonious fits)
        let weightAdjustment = 0;
        switch (fb.rating) {
          case "✨":
            weightAdjustment = 15;
            sublimeCount++;
            break;
          case "🙂":
            weightAdjustment = 6;
            balancedCount++;
            break;
          case "😕":
            weightAdjustment = -25;
            recalibrateCount++;
            break;
          default:
            weightAdjustment = 0;
            break;
        }

        for (const itemId of fb.items) {
          itemScores[itemId] = (itemScores[itemId] || 0) + weightAdjustment;
          itemCounts[itemId] = (itemCounts[itemId] || 0) + 1;
        }
      }

      // 3. Normalize model weights for each clothing item
      const trainedItemWeights: { [itemId: string]: number } = {};
      for (const itemId in itemScores) {
        if (Object.prototype.hasOwnProperty.call(itemScores, itemId)) {
          // Normalize the total weight to stay within reasonable boundary thresholds [-30, 20]
          const rawWeight = itemScores[itemId];
          trainedItemWeights[itemId] = Math.max(-30, Math.min(20, rawWeight));
        }
      }

      // 4. Generate metadata statistics for the retrained model
      const totalRatings = feedbacks.length;
      const modelMetadata = {
        lastRetrainedAt: admin.firestore.Timestamp.now(),
        totalSamplesAnalyzed: totalRatings,
        sentimentDistribution: {
          sublime: sublimeCount,
          balanced: balancedCount,
          recalibrate: recalibrateCount,
        },
        confidenceScore: Math.min(100, Math.round((totalRatings / 10) * 100)), // Confidence scales with sample count
      };

      // 5. Save the final compiled weights & neural metadata back to Firestore
      // The client app will read this document to apply live styling corrections in the scoring engine!
      const modelPayload = {
        metadata: modelMetadata,
        itemAffinities: trainedItemWeights,
      };

      await db.collection("outfitModel").doc("trained_weights").set(modelPayload);

      functions.logger.info("Successfully retrained SARTORIAL AURA scoring model weights.", {
        metadata: modelMetadata,
        uniqueItemWeightsUpdated: Object.keys(trainedItemWeights).length,
      });

      return { success: true, retrainedItemsCount: Object.keys(trainedItemWeights).length };
    } catch (error) {
      functions.logger.error("Thermal retraining pipeline failure: ", error);
      throw new functions.https.HttpsError("internal", "Retraining pipeline met an unexpected processing crash.");
    }
  });

/**
 * Cloud Function triggered on any write to the 'wearFeedback' collection.
 * Parses the satisfaction score/rating and updates the corresponding clothing
 * items' harmony weights in the 'user_preferences/harmony_weights' document in Firestore.
 */
export const updateUserPreferencesHarmony = functions.firestore
  .document("wearFeedback/{feedbackId}")
  .onWrite(async (change, context) => {
    const feedbackId = context.params.feedbackId;
    functions.logger.info(`Starting updateUserPreferencesHarmony for feedbackId: ${feedbackId}`);

    if (!change.after.exists) {
      functions.logger.info(`Feedback document ${feedbackId} was deleted. No action needed.`);
      return null;
    }

    const data = change.after.data();
    if (!data) {
      functions.logger.info("Feedback document data is empty. Skipping.");
      return null;
    }

    const items: string[] = data.items || [];
    const rating: string = data.rating || "";
    const score: number = Number(data.score) || 0;

    if (items.length === 0) {
      functions.logger.info("No items provided in feedback. Skipping.");
      return null;
    }

    // Determine weight adjustment based on user satisfaction rating emoji or numeric score
    let weightAdjustment = 0;
    switch (rating) {
      case "✨": // Sublime
        weightAdjustment = 15;
        break;
      case "🙂": // Balanced
        weightAdjustment = 5;
        break;
      case "😕": // Recalibrate
        weightAdjustment = -20;
        break;
      default:
        // Score-based fallback if rating emoji is not standard
        if (score >= 85) {
          weightAdjustment = 15;
        } else if (score >= 60) {
          weightAdjustment = 5;
        } else {
          weightAdjustment = -20;
        }
        break;
    }

    functions.logger.info(`Feedback rating is '${rating}' with score ${score}. Selected weight adjustment: ${weightAdjustment}`);

    const userPreferencesRef = db.collection("user_preferences").doc("harmony_weights");

    try {
      await db.runTransaction(async (transaction) => {
        const doc = await transaction.get(userPreferencesRef);
        const currentWeights = doc.exists ? (doc.data()?.weights || {}) : {};

        // Deep copy and apply the weight transformations to the clothing items
        const updatedWeights = { ...currentWeights };
        for (const itemId of items) {
          const currentWeight = updatedWeights[itemId] || 0;
          // Constrain item weights to a defined envelope to maintain balanced recommendations
          updatedWeights[itemId] = Math.max(-50, Math.min(50, currentWeight + weightAdjustment));
        }

        const payload = {
          weights: updatedWeights,
          lastFeedbackId: feedbackId,
          lastUpdatedAt: admin.firestore.Timestamp.now(),
          lastSatisfactionScore: score,
          lastRating: rating,
        };

        transaction.set(userPreferencesRef, payload, { merge: true });
      });

      functions.logger.info(`Successfully updated user_preferences/harmony_weights for items: ${items.join(", ")}`);
      return { success: true };
    } catch (error) {
      functions.logger.error("Error updating user_preferences harmony weights: ", error);
      return null;
    }
  });

