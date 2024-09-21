const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const corsConfig = require('./config/cors');
const limiter = require('./config/limiter');
const pool = require('./config/db');

const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
const roleRoutes = require('./routes/roleRoutes');


require('dotenv').config();
dotenv.config();

const app = express();

app.use(cors(corsConfig));

app.use(express.json());

app.use(limiter);

// routes:
app.use('/auth', authRoutes);
app.use('/user', userRoutes);
app.use('/role', roleRoutes);


app.get('/', (req, res) => {
    res.send('Server running with connection to PostgreSQL');
});

const port = process.env.PORT || 8000;
app.listen(port, () => {
    console.log(`Server running on the port ${port}`);
});

module.exports = app;