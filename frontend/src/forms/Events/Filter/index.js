import React from 'react';
import PropTypes from "prop-types";

import {FORM_DTO} from 'forms/utils/constants'
import CustomButton from "forms/utils/components/CustomButton";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {connect} from "App/Context";

import {setFilter, setPage} from "../actions";
import Locations from "./Locations";
import Interests from "./Interests";
import Languages from "./Languages";
import Dates from "./Dates";

const Filter = ({setFilter}) => {
    const clearFilters = () => {
        setFilter({...FORM_DTO("event")});
        setPage(0);
    };

    return <Container>
        <Locations/>
        <ItemContainer>
            <Dates/>
        </ItemContainer>
        <ItemContainer>
            <Interests/>
        </ItemContainer>
        <ItemContainer>
            <Languages/>
        </ItemContainer>
        <ItemContainer>
            <CustomButton onClick={clearFilters} color="primary" text='Clear'/>
        </ItemContainer>
    </Container>
};

Filter.propTypes = {
    setFilter: PropTypes.func.isRequired
};

export default connect(null,
    {setPage, setFilter})(Filter);