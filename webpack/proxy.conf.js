function setupProxy({ tls }) {
  const serverResources = ['/api', '/services', '/management', '/v3/api-docs', '/h2-console', '/health'];
  const ibgeResources = ['/api/ibge'];
  return [
    {
      context: serverResources,
      target: `http${tls ? 's' : ''}://localhost:8080`,
      secure: false,
      changeOrigin: tls,
    },
    {
      context: ibgeResources,
      target: 'https://servicodados.ibge.gov.br',
      secure: true,
      changeOrigin: true,
      pathRewrite: {
        '^/api/ibge': '', 
      },
    },
  ];
}

module.exports = setupProxy;
