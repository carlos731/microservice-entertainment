const express = require('express');
const router = express.Router();
const userController = require('../controller/userController');
const { body } = require('express-validator');

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

// AuthRoutes:
router.post('/auth/login', validateLogin, userController.login);
router.post('/auth/register', validateRegister, userController.register);
router.post('/auth/refresh-token', userController.refreshToken);
router.get('/auth/verify-token', userController.verifyToken);
// router.post('/auth/forgot-password', authController.forgotPassword);
// router.post('/auth/verify-otp', authController.verifyPasswordResetOTP);
// router.post('/auth/reset-password', authController.resetPassword);

// UserRoutes:
router.get('/user/findAll', userController.getAllUsers);

module.exports = router;