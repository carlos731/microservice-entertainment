import express from 'express';
import { connectToDatabase } from './db';
import fileRoutes from './routes/fileRoutes';
import swaggerUi from 'swagger-ui-express';
import swaggerDocs from './config/swaggerConfig';

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
    await connectToDatabase();
    app.listen(port, () => {
      console.log(`Server running at http://localhost:${port}/`);
      console.log('Documentação disponível em http://localhost:9000/api-docs');
    });
  } catch (error) {
    console.error('Failed to start server due to database connection issues');
  }
};

startServer();