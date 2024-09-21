const express = require('express');
const router = express.Router();
const authController = require('../controller/authController');
const { body } = require('express-validator');
const { authenticateToken, authorizePermissions } = require('../middleware/authenticateToken');

const validateRegister = [
    body('firstname').not().isEmpty().withMessage('Name is required'),
    body('lastname').not().isEmpty().withMessage('Lastname is required'),
    body('email').isEmail().withMessage('Please enter a valid email address'),
    body('password')
        .isLength({ min: 8 }).withMessage('Password must be at least 8 characters')
        .isStrongPassword().withMessage('Password must contain at least one uppercase, one lowercase, and one symbol.')
];

const validateLogin = [
    body('email').isEmail().withMessage('Please enter a valid email address'),
    body('password')
        .isLength({ min: 8 }).withMessage('Password must be at least 8 characters')
        .isStrongPassword().withMessage('Password must contain at least one uppercase, one lowercase, and one symbol.')
]

router.post('/login', validateLogin, authController.login);
router.post('/register', validateRegister, authController.register);
router.post('/refresh-token', authController.refreshToken);
router.get('/verify-token', authController.verifyToken);
router.post('/forgot-password', authController.forgotPassword);
router.post('/verify-otp', authController.verifyOtp);
router.put('/reset-password', authenticateToken, authController.resetPassword);

module.exports = router;