import { Router } from 'express';
import multer from 'multer';
import { 
    uploadFile, 
    uploadMultipleFiles, 
    getFiles, 
    getFileById, 
    deleteFileById,
    getVideoById, 
    getImageById,
    downloadFileById
} from '../controllers/fileController';
import { authenticateToken, authorizePermissions } from '../middleware/authenticateToken';

const router = Router();

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

router.post('/upload-multiple', authenticateToken, authorizePermissions('upload_file'), upload.array('files'), uploadMultipleFiles);
router.post('/upload', /*authenticateToken, authorizePermissions('upload_file'),*/ upload.single('file'), uploadFile);
router.get('/files', /*authenticateToken,*/ getFiles);
router.get('/file/:id', getFileById);
router.get('/video/:id', /*authenticateToken,*/ getVideoById);
router.get('/image/:size?/:id', /*authenticateToken,*/ getImageById);
router.delete('/delete/:id', authenticateToken, authorizePermissions('delete_file'), deleteFileById);
router.get('/download/:id', downloadFileById);

export default router;