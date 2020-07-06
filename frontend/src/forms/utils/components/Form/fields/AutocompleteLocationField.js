import React from 'react';

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";

const AutocompleteLocationField = ({name, placeholder, setValue, error}) => {
    return <AutocompleteLocation onChangeLocation={(location) => setValue(name, location)}
                                 placeholder={placeholder}
                                 name='location'
                                 error={error}
    />
};

export default AutocompleteLocationField;