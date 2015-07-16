import config from '../../config/environment';

export default function(locale, key, context) {
    if (config.environment === 'development') {
        console.log('Missing translations: ' + key);
    }

    return 't[' + key + ']';
}
