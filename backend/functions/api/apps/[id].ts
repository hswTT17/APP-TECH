import { apps, corsHeaders } from '../../_shared/data';

export const onRequestGet: PagesFunction = async (context) => {
  const id = context.params.id as string;
  const app = apps.find((a) => a.id === id);

  if (!app) {
    return new Response(JSON.stringify({ error: 'App not found' }), {
      status: 404,
      headers: corsHeaders,
    });
  }

  return new Response(JSON.stringify(app), { headers: corsHeaders });
};

export const onRequestOptions: PagesFunction = async () => {
  return new Response(null, { headers: corsHeaders });
};
