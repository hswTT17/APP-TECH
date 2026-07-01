import { apps, corsHeaders } from '../_shared/data';

export const onRequestGet: PagesFunction = async () => {
  const summary = apps.map(({ id, name, link, iconUrl, category, benefits }) => ({
    id,
    name,
    link,
    iconUrl,
    category,
    benefitCount: benefits.length,
  }));

  return new Response(JSON.stringify(summary), { headers: corsHeaders });
};

export const onRequestOptions: PagesFunction = async () => {
  return new Response(null, { headers: corsHeaders });
};
