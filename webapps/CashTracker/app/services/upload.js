import Em from 'ember';

const {
    Service, $
} = Em;

const PATH = 'rest/file/upload';

export default Service.extend({

    upload: function(attachInput) {
        var formData = new FormData();
        formData.append('file', attachInput[0].files[0]);

        return $.ajax({
            url: PATH,
            method: 'POST',
            cache: false,
            contentType: false,
            processData: false,
            data: formData,
            success: function(result) {
                return result;
            },
            error: function(err) {
                console.log(err);
            }
        });
    }
});
