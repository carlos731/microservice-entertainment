const { validationResult } = require('express-validator');
const Role = require('../models/role');
const bcrypt = require('bcryptjs');

const saltRounds = parseInt(process.env.BYCRIPT) || 10;

class RoleController {
    static async addRole(req, res) {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            const errorMessages = errors.array().map((error) => ({
                field: error.path,
                message: error.msg,
            }));
            return res.status(400).json({ errors: errorMessages });
        }

        const { name } = req.body;

        try {
            const existingRole = await Role.findByName(name);
            if (existingRole) {
                return res.status(400).json({ error: 'Role já existe.' });
            }

            const hashedName = await bcrypt.hash(name, saltRounds);
            const newRole = await Role.create(name);

            res.status(201).json(newRole);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao adicionar usuário.' });
        }
    }

    static async deleteById(req, res) {
        const { id } = req.params;

        try {
            const deleted = await Role.deleteById(id);
            if (!deleted) {
                return res.status(404).json({ error: 'Role não encontrado.' });
            }
            res.status(204).send();
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: 'Error ao deleter usuário.' });
        }
    }
}

module.exports = RoleController;