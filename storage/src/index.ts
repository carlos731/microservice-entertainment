import express from 'express';
import { connectToDatabase } from './db';
import fileRoutes from './routes/fileRoutes';

const app = express();
const port = 9000;

app.use(express.json());

app.get('/', (req, res) => {
  res.send('Hello World!');
});

app.use(express.json());

app.use('/storage', fileRoutes);

const startServer = async () => {
  try {
    await connectToDatabase();
    app.listen(port, () => {
      console.log(`Server running at http://localhost:${port}/`);
    });
  } catch (error) {
    console.error('Failed to start server due to database connection issues');
  }
};

startServer();