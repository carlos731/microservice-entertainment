import express from 'express';
import { createPool } from './db';
import fileRoutes from './routes/fileRoutes';
import swaggerUi from 'swagger-ui-express';
import swaggerDocs from './config/swaggerConfig';
import configServer from './config/configServer';
import { getServerPort } from './context/configContext';

const app = express();
const port = 9000;

app.use(express.json());

app.get('/', (req, res) => {
  res.send('Hello World!');
});

app.use(express.json());

app.use('/storage', fileRoutes);
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));

const startServer = async () => {
  try {
    await configServer();

    // await connectToDatabase();

    await createPool();

    app.listen(getServerPort(), () => {
      console.log(`Server running at http://localhost:${getServerPort()}/`);
      console.log(`Documentação disponível em http://localhost:${getServerPort()}/api-docs`);
    });
  } catch (error) {
    console.error('Failed to start server due to database connection issues');
  }
};

startServer();