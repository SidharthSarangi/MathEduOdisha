import { EventType as Event } from '../types/eventType';
import bannerImage from '../assets/bannerImage.jpg'
import bannerPhoto from '../assets/bannerPhoto.jpg'

export const fetchEvents = async (): Promise<Event[]> => {
  return [
    {
      _id: "1",
      title: "Current Event 1",
      date: new Date().toISOString(),
      description: "Details about current event 1...",
      imageURL: bannerImage,
    },
    {
      _id: "2",
      title: "Upcoming Event 2",
      date: new Date(
        new Date().setDate(new Date().getDate() + 7)
      ).toISOString(),
      description: "Details about upcoming event 2...",
      imageURL: bannerPhoto,
    },
    {
      _id: "3",
      title: "Past Event 3",
      date: new Date(
        new Date().setDate(new Date().getDate() + 7)
      ).toISOString(),
      description: "Details about Past event 3...",
      imageURL: bannerImage,
    },
    {
      _id: "4",
      title: "Happening Event 4",
      date: new Date(
        new Date().setDate(new Date().getDate() + 7)
      ).toISOString(),
      description: "Details about Happening event 4...",
      imageURL: bannerPhoto,
    },
  ];
};
