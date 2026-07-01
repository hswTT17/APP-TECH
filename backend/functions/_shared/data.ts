import appsData from '../../data/apps.json';

export interface Benefit {
  title: string;
  description: string;
  howTo: string[];
}

export interface AppEntry {
  id: string;
  name: string;
  link: string;
  iconUrl: string | null;
  category: string;
  benefits: Benefit[];
}

export const apps = appsData as AppEntry[];

export const corsHeaders = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Methods': 'GET, OPTIONS',
  'Content-Type': 'application/json; charset=utf-8',
};
