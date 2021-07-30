ClassicEditor
    .create(document.querySelector('#contents'), {
        extraPlugins: [MyCustomUploadAdapterPlugin]
    })
    .then(editor => {
        console.log(editor);
    })
    .catch(error => {
        console.log(error);
    });

function MyCustomUploadAdapterPlugin(editor) {
    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
        return new UploadAdapter(loader)
    }
}