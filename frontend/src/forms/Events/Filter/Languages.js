import React, {useEffect, useState} from 'react';

import {connect} from "App/Context";

import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import {FilterOperator} from "forms/utils/utils";

import {getLanguages, setFilter} from "../actions";

const Languages = ({languages, getLanguages, setFilter}) => {
    const [chooseLanguages, setChooseLanguages] = useState([]);

    useEffect(() => {
        getLanguages();
    }, [getLanguages]);

    const onChange = (languages) => {
        const searchField = "author?languages.id";
        setChooseLanguages(languages);
        setFilter(FilterOperator.EQUAL,
            languages.map(language => ({id: language.id})),
            searchField);
    }

    return <LoadableContent loadableData={languages}>
        <MultipleSelectBox onChange={onChange}
                           value={chooseLanguages}
                           optionsSimple={languages.response}
                           label='Languages'/>
    </LoadableContent>;
};

const mapStateToProps = state => ({
    languages: state.components.forms.events.languages
});

export default connect(mapStateToProps, {getLanguages, setFilter})(Languages);