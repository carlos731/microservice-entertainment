const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const corsConfig = require('./config/cors');
const pool = require('./config/db');

const route = require('./routes/authRoutes');
const userRoute = require('./routes/userRoutes');


require('dotenv').config();
dotenv.config();

const app = express();

app.use(cors(corsConfig));

app.use(express.json());

// routes:
app.use('/auth', route);
app.use('/user', userRoute);

app.get('/', (req, res) => {
    res.send('Server running with connection to PostgreSQL');
});

const port = process.env.PORT || 8000;
app.listen(port, () => {
    console.log(`Server running on the port ${port}`);
});

module.exports = app;