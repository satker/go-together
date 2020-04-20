export const setFormId = (state, setState) => (formId) => () => {
    setState('formId', formId);
};