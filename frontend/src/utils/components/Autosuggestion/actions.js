export const getOptions = () => (url, urlParam, value, setLoading) => (fetch) => {
    fetch(url + '?' + urlParam + '=' + value, () => {
        setLoading(false);
    });
};