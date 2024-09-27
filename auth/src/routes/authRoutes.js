const express = require('express');
const router = express.Router();
const authController = require('../controller/authController');
const { body } = require('express-validator');
const { authenticateToken, authorizePermissions } = require('../middleware/authenticateToken');
const multer = require('multer');

const { uploadFile } = require('../services/uploadService');

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

const validateRegister = [
    body('firstname').not().isEmpty().withMessage('Firstname is required'),
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
router.post('/register', validateRegister, upload.single('avatar'), authController.register);
router.post('/refresh-token', authController.refreshToken);
router.get('/verify-token', authController.verifyToken);
router.post('/forgot-password', authController.forgotPassword);
router.post('/verify-otp', authController.verifyOtp);
router.put('/reset-password', authenticateToken, authController.resetPassword);

router.post('/upload', upload.single('avatar'), async (req, res) => {
    if (!req.file) {
        return res.status(400).json({ message: 'Por favor, envie um arquivo.' });
    }

    const { buffer, originalname, mimetype } = req.file;
    const { firstname, lastname, email } = req.body;

    if (!firstname) {
        return res.status(400).json({ message: 'Informe o firstname'} );
    }

    try {
        // Call the uploadFile service
        const fileUrl = await uploadFile({ filename: originalname, contentType: mimetype, buffer });

        res.status(200).json({
            message: 'Arquivo enviado com sucesso!',
            url: fileUrl, // URL returned by the uploadFile service
        });
    } catch (error) {
        console.error('Erro ao fazer upload do arquivo:', error.message);
        res.status(500).json({ message: 'Erro ao fazer upload do arquivo.', error: error.message });
    }
});

module.exports = router;