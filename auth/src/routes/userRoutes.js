const express = require('express');
const router = express.Router();
const userController = require('../controller/userController');
const { body } = require('express-validator');
const { authenticateToken, authorizePermissions } = require('../middleware/authenticateToken');

router.post('/add', authenticateToken, authorizePermissions('create:user'), userController.addUser);
router.put('/update', authenticateToken, authorizePermissions('update:user'), userController.updateUser);
router.get('/:id', authenticateToken, authorizePermissions('update:user'), userController.getUserById);
router.delete('/:id', authenticateToken, authorizePermissions('delete:user'), userController.deleteUser);
router.get('', userController.getAllUsers);

module.exports = router;