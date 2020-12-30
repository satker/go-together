import React, {useState} from 'react';
import PropTypes from 'prop-types';

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ItemContainer from "../../Container/ItemContainer";

const AutocompleteLocationField = ({name, placeholder, setValue, error, value}) => {
    const [currentValue, setCurrentValue] = useState();

    const onChange = (latLng, loc) => {
        let location = {
            ...value,
            address: loc.name,
            latitude: latLng.lat,
            longitude: latLng.lng,
            place: loc
        };
        setValue(name, location);
    };
    console.log(value)
    return <ItemContainer style={{height: 70}}>
        <AutocompleteLocation onChangeLocation={(place, {lat, lng}) => onChange({lat, lng}, place)}
                              placeholder={placeholder}
                              setValueCenter={setCurrentValue}
                              value={currentValue || (value && value.place)}
                              name='place'
                              error={error}
        />
    </ItemContainer>
};

AutocompleteLocationField.propTypes = {
    value: PropTypes.array
}

export default AutocompleteLocationField;