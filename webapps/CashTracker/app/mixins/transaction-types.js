import Em from 'ember';

export default Em.Mixin.create({
    transactionTypes: [{
        value: 'E',
        label: 'expense'
    }, {
        value: 'I',
        label: 'income'
    }, {
        value: 'T',
        label: 'transfer'
    }]
});
