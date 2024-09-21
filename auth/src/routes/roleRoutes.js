const express = require('express');
const router = express.Router();
const roleController = require('../controller/roleController');
const { body } = require('express-validator');
const { authenticateToken, authorizePermissions } = require('../middleware/authenticateToken');

const validateRole = [
    body('name').not().isEmpty().withMessage('Name is required'),
]

router.post('/add', validateRole, authenticateToken, authorizePermissions('create:role'), roleController.addRole);
router.delete('/:id', authenticateToken, authorizePermissions('delete:role'), roleController.deleteById);

module.exports = router;