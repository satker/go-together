import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";

const AddressFields = ({google, setCenter}) => {
    const [autocompleteInput] = useState(React.createRef);

    useEffect(() => {
        const options = {
            types: ['(cities)'],
        };

        const autocompleteCustom = new google.maps.places.Autocomplete(autocompleteInput.current, options);

        autocompleteCustom.setFields(['address_components', 'formatted_address', 'geometry']);

        autocompleteCustom.addListener('place_changed', () => {
            const addressObject = autocompleteCustom.getPlace();
            setCenter([addressObject.geometry.location.lat(), addressObject.geometry.location.lng()]);
        });
    }, [autocompleteInput, google, setCenter]);

    return (
        <div className="form-group">
            <label htmlFor="">Country</label>
            <input type="text"
                   id='autocomplete'
                   name="autocomplete"
                //onChange={evt => handlePlaceSelect(evt.target.value)}
                   className="form-control"
                   ref={autocompleteInput}
            />
        </div>
    );
};

AddressFields.propTypes = {
    google: PropTypes.object.isRequired,
    setCenter: PropTypes.func.isRequired
};

export default AddressFields;