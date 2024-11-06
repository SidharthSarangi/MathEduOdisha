// src/services/donationService.ts
import { getTopDonations } from '../api/donationApi';
import { Donation } from '../types/donationType';

export const fetchTopDonations = async (): Promise<Donation[]> => {
  try {
    const donations = await getTopDonations();
    // Additional logic or transformations can be added here if needed
    return donations;
  } catch (error) {
    console.error("Error in donation service:", error);
    return [];
  }
};
