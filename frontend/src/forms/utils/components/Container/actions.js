export const setFormId = (state, setState) => (formId) => () => {
    console.log(setState, formId);
    setState('formId', formId);
};