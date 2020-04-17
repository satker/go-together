export const getOptions = () => (url, urlParam, value) => (fetch) => {
    fetch(url + '?' + urlParam + '=' + value);
};