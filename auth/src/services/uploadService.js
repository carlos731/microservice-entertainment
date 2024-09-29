const axios = require('axios');
const FormData = require('form-data');

async function uploadFile({ filename, contentType, buffer }) {
    const formData = new FormData();
    formData.append('file', buffer, {
        filename: filename,
        contentType: contentType,
    });

    try {
        const response = await axios.post(process.env.STORAGE_URL, formData, {
            headers: {
                ...formData.getHeaders(),
                'x-api-key': process.env.STORAGE_KEY,
            },
        });

        return response.data.url; // Return the URL directly
    } catch (error) {
        console.error('Error uploading file', error.message);
        // throw error;
    }
}

module.exports = { uploadFile };