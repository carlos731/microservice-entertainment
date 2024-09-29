const express = require('express');
const router = express.Router();
const roleController = require('../controller/roleController');
const { body } = require('express-validator');
const { authenticateToken, authorizePermissions } = require('../middleware/authenticateToken');

const validateRole = [
    body('name').not().isEmpty().withMessage('Name is required'),
]

const validateUpdateRole = [
    body('id').not().isEmpty().withMessage('Id is required.'),
    body('name').not().isEmpty().withMessage('Name is required.'),
]

router.post('/', validateRole, authenticateToken, authorizePermissions('create:role'), roleController.addRole);
router.put('/', validateUpdateRole, authenticateToken, authorizePermissions('update:role'), roleController.updateById);
router.get('/', /*authenticateToken, authorizePermissions('view:role'),*/ roleController.findAll);
router.get('/:id', authenticateToken, authorizePermissions('view:role'), roleController.findById);
router.delete('/:id', authenticateToken, authorizePermissions('delete:role'), roleController.deleteById);

module.exports = router;