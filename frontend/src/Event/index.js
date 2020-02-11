import React from 'react';
import GetAndViewApartment from "./View";
import CreateApartment from "./Edit";
import * as PropTypes from "prop-types";

const Apartment = ({id, isView}) => isView ?
    <GetAndViewApartment id={id}/> :
    <CreateApartment id={id}/>;

Apartment.propTypes = {
    id: PropTypes.string,
    isView: PropTypes.bool.isRequired
};

export default Apartment;