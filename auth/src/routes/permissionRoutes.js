const express = require('express');
const router = express.Router();
const permissionController = require('../controller/permissionController');
const { body } = require('express-validator');
const { authenticateToken, authorizePermissions } = require('../middleware/authenticateToken');

const validatePermission = [
    body('name').not().isEmpty().withMessage('Name is required'),
    body('description').not().isEmpty().withMessage('Description is required'),
]

const validateUpdatePermission = [
    body('id').not().isEmpty().withMessage('Id is required.'),
    body('name').not().isEmpty().withMessage('Name is required'),
    body('description').not().isEmpty().withMessage('Description is required'),
]

router.post('/', validatePermission, authenticateToken, authorizePermissions('create:permission'), permissionController.add);
router.put('/', validateUpdatePermission, authenticateToken, authorizePermissions('update:permission'), permissionController.updateById);
router.get('/', authenticateToken, authorizePermissions('view:permission'), permissionController.findAll);
router.get('/:id', authenticateToken, authorizePermissions('view:permission'), permissionController.findById);
router.delete('/:id', authenticateToken, authorizePermissions('delete:permission'), permissionController.deleteById);

module.exports = router;