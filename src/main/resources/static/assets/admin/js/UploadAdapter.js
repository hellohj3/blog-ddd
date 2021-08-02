class UploadAdapter {
    constructor(loader) {
        // CKEditor5's FileLoader instance.
        this.loader = loader;
    }
    // Starts the upload process.
    upload() {
        return new Promise((resolve, reject) => {
            this._initRequest();
            this._initListeners(resolve, reject);
            this._sendRequest();
        });
    }
    // Aborts the upload process.
    abort() {
        if (this.xhr) {
            this.xhr.abort();
        }
    }
    // Example implementation using XMLHttpRequest.
    _initRequest() {
        const xhr = (this.xhr = new XMLHttpRequest());
        xhr.open("POST", '/admin/attachments', true);
        xhr.responseType = "json";
    }
    // Initializes XMLHttpRequest listeners.
    _initListeners(resolve, reject) {
        const xhr = this.xhr;
        const loader = this.loader;
        const genericErrorText = "Couldn't upload file:" + ` ${loader.file.name}.`;
        xhr.addEventListener("error", () => reject(genericErrorText));
        xhr.addEventListener("abort", () => reject());
        xhr.addEventListener("load", () => {
            const response = xhr.response;
            // console.log(response);
            if (!response || response.error) {
                return reject(
                    response && response.error ? response.error.message : genericErrorText
                );
            }
            // If the upload is successful, resolve the upload promise with an object containing
            // at least the "default" URL, pointing to the image on the server.
            resolve({
                default: response[0].path + "/" + response[0].name,
            });
        });
        if (xhr.upload) {
            xhr.upload.addEventListener("progress", (evt) => {
                if (evt.lengthComputable) {
                    loader.uploadTotal = evt.total;
                    loader.uploaded = evt.loaded;
                }
            });
        }
    }
    // Prepares the data and sends the request.
    _sendRequest() {
        const data = new FormData();
        let that = this;
        let file = that.loader.file;

        file.then(function (result) {
            //wait for the promise to finish then continue
            data.append("upload", result);
            that.xhr.send(data);
        });
    }
}