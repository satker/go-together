import React from 'react';
import PropTypes from "prop-types";
import CustomButton from "forms/utils/components/CustomButton";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {connect} from "App/Context";

import {cleanFilter, setPage} from "../actions";
import Locations from "./Locations";
import Interests from "./Interests";
import Languages from "./Languages";
import Dates from "./Dates";

const Filter = ({cleanFilter}) => {
    const clearFilters = () => {
        cleanFilter();
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
    cleanFilter: PropTypes.func.isRequired,
    setPage: PropTypes.func.isRequired
};

export default connect(null,
    {setPage, cleanFilter})(Filter);