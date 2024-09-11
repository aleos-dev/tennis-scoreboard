document.addEventListener('DOMContentLoaded', function() {
    var fileInput = document.getElementById('image-input');
    if (fileInput) {
        fileInput.addEventListener('change', function() {
            const maxAllowedSize = 2 * 1024 * 1024; // 2 MB
            if (this.files.length > 0 && this.files[0].size > maxAllowedSize) {
                alert('File size should not exceed 2 MB.');
                this.value = '';
            }
        });
    }
});
