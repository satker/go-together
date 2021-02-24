import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import {AppBar, Tabs} from "@material-ui/core";
import {groupBy, mapValues} from 'lodash';

import GroupItems from "forms/utils/components/CardItems";

import TabPanel from "./TabPanel";
import NavTab from "./NavTab";
import './style.css'

const ElementTabs = (props) => {
    const {tabs, elements, elementsFieldTab} = props;
    const [activeTab, setActiveTab] = useState(0);
    const [tabElements, setTabElements] = useState([]);

    const handleChange = (event, newValue) => {
        if (activeTab !== newValue) {
            setActiveTab(newValue);
        }
    };

    useEffect(() => {
        if (elements && elements.length !== 0) {
            const groupElements = mapValues(groupBy(elements, elementsFieldTab));
            setTabElements(groupElements);
        } else {
            setTabElements([]);
        }
    }, [setTabElements, elements, elementsFieldTab]);


    return <div className='flex element-tabs custom-border' style={{height: '300px'}}>
        <AppBar position="static" color="default">
            <Tabs value={activeTab} onChange={handleChange}>
                {tabs.map((tab, index) =>
                    <NavTab onChange={handleChange} key={index} name={tab} index={index}/>)}
            </Tabs>
        </AppBar>
        {tabs.map((tab, index) => <TabPanel key={index} value={activeTab} index={index}>
            {tabElements[tab] ? <GroupItems items={tabElements[tab]} {...props}/> : 'Not found'}
        </TabPanel>)}
    </div>
};

ElementTabs.propTypes = {
    tabs: PropTypes.array.isRequired,
    elements: PropTypes.array,
    elementsFieldTab: PropTypes.string.isRequired
};

export default ElementTabs;