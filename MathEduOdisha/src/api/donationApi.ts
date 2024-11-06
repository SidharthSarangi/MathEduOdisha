import axios from 'axios';
import { Donation } from '../types/donationType';

// Sample Donation[] data
const donations: Donation[] = [
    {
      id: '1',
      donorName: 'Alice Johnson',
      amount: 500.00,
      date: '2024-10-15'
    },
    {
      id: '2',
      donorName: 'Bob Smith',
      amount: 300.00,
      date: '2024-10-10'
    },
    {
      id: '3',
      donorName: 'Catherine Lee',
      amount: 250.50,
      date: '2024-09-28'
    },
    {
      id: '4',
      donorName: 'David Chen',
      amount: 200.00,
      date: '2024-09-20'
    },
    {
      id: '5',
      donorName: 'Ella Brown',
      amount: 150.00,
      date: '2024-09-10'
    },
    {
      id: '6',
      donorName: 'Frank White',
      amount: 100.75,
      date: '2024-08-25'
    },
    {
      id: '7',
      donorName: 'Grace Kim',
      amount: 75.25,
      date: '2024-08-15'
    },
    {
      id: '8',
      donorName: 'Henry Wilson',
      amount: 50.00,
      date: '2024-08-05'
    },
  ];
  


export const getTopDonations = async (): Promise<Donation[]> => {
  try {
    // const response = await axios.get('/api/donations/top');
    // return response.data;
    return donations ;
  } catch (error) {
    console.error("Error fetching top donations:", error);
    throw error;
  }
};
