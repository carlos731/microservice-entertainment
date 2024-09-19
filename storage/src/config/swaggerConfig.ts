import swaggerJSDoc from 'swagger-jsdoc';
import { SwaggerDefinition } from 'swagger-jsdoc';

const swaggerDefinition: SwaggerDefinition = {
  openapi: '3.0.0',
  info: {
    title: 'Storage API',
    version: '1.0.0',
    description: 'API para gerenciamento de arquivos no storage',
    contact: {
      name: 'Carlos Henrique',
      email: 'carlos@gmail.com',
    },
  },
  servers: [
    {
      url: 'http://localhost:9000/storage',
    },
  ],
};

const options = {
  swaggerDefinition,
  apis: ['src/routes/*.ts'],
};

const swaggerDocs = swaggerJSDoc(options);

export default swaggerDocs;