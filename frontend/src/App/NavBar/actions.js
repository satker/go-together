export const setUserIdAndFetchWithToken = (state, setState) => (fields, values) => () => {
    setState(fields, values);
};