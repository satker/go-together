import React, {useEffect, useState} from 'react';

import {connect} from "App/Context";

import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import {FilterOperator} from "forms/utils/utils";

import {getInterests, setFilter} from "../actions";

const Interests = ({interests, getInterests, setFilter}) => {
    const [chooseInterests, setChooseInterests] = useState([]);

    useEffect(() => {
        getInterests();
    }, [getInterests]);

    const onChange = (interests) => {
        const searchField = "author?interests.id";
        setChooseInterests(interests);
        setFilter(FilterOperator.EQUAL,
            interests.map(interest => ({id: interest.id})),
            searchField);
    }

    return <LoadableContent loadableData={interests}>
        <MultipleSelectBox onChange={onChange}
                           label='Interests'
                           optionsSimple={interests.response}
                           value={chooseInterests}/>
    </LoadableContent>;
};

const mapStateToProps = () => state => ({
    interests: state.components.forms.events.interests,
});

export default connect(mapStateToProps, {getInterests, setFilter})(Interests);