const { validationResult } = require('express-validator');
const Permission = require('../models/permission');
const bcrypt = require('bcryptjs');

const saltRounds = parseInt(process.env.BYCRIPT) || 10;

class PermissionController {
    static async add(req, res) {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            const errorMessages = errors.array().map((error) => ({
                field: error.path,
                message: error.msg,
            }));
            return res.status(400).json({ errors: errorMessages });
        }

        const { name, description } = req.body;

        try {
            const existingPermission = await Permission.findByName(name);
            if (existingPermission) {
                return res.status(400).json({ error: 'Permission já existe.' });
            }

            const hashedName = await bcrypt.hash(name, saltRounds);
            const newPermission = await Permission.create(name, description);

            res.status(201).json(newPermission);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Erro ao adicionar usuário.' });
        }
    }

    static async updateById(req, res) {
        const { id, name, description } = req.body;

        try {
            const permission = await Permission.findById(id);
            if (!permission) {
                return res.status(400).json({ error: `Permission not found with id ${id}.` });
            }
            const updatePermission = await Permission.updateById(id, name, description);
            res.status(200).json(updatePermission);
        } catch (error) {
            console.log(error);
            res.status(500).json({ message: "Erro ao atualizar permission." });
        }
    }

    static async findAll(req, res) {
        try {
            const permissions = await Permission.findAll();
            res.status(200).json(permissions);
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: "Erro ao buscar permissions" });
        }
    }

    static async findById(req, res) {
        const { id } = req.params;

        try {
            const permission = await Permission.findById(id);
            if (!permission) {
                return res.status(404).json({ message: "Permission not found." });
            }
            res.status(200).json(permission);
        } catch (error) {
            console.log(error);
            res.status(500).json({ message: "Erro ao buscar a permission. " });
        }
    }

    static async deleteById(req, res) {
        const { id } = req.params;

        try {
            const deleted = await Permission.deleteById(id);
            if (!deleted) {
                return res.status(404).json({ error: 'Permission not found.' });
            }
            res.status(204).send();
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: 'Error ao deleter usuário.' });
        }
    }
}

module.exports = PermissionController;