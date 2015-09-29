import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
    attrs: {
        attachments: {
            embedded: 'always'
        },
        account: {
            embedded: 'always'
        },
        transferAccount: {
            embedded: 'always'
        },
        category: {
            embedded: 'always'
        },
        costCenter: {
            embedded: 'always'
        },
        tags: {
            embedded: 'always'
        }
    }
});
