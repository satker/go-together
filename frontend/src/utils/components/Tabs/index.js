import React, {useEffect, useState} from "react";
import {Nav, TabContent, TabPane} from "reactstrap";
import NavTab from "./NavTab";
import PropTypes from "prop-types";
import {groupBy, mapValues, omit} from 'lodash';
import GroupItems from "../CardItems";

const ElementTabs = (props) => {
    const {tabs, elements, elementsFieldTab} = props;
    const [activeTab, setActiveTab] = useState(tabs[0]);
    const [tabElements, setTabElements] = useState([]);

    const toggle = (tab) => {
        if (activeTab !== tab) {
            setActiveTab(tab);
        }
    };

    useEffect(() => {
        if (elements && elements.length !== 0) {
            const groupElements = mapValues(groupBy(elements, elementsFieldTab),
                element => element.map(car => omit(car, elementsFieldTab)));
            setTabElements(groupElements);
        } else {
            setTabElements([]);
        }
    }, [setTabElements, elements, elementsFieldTab]);
    console.log(tabElements)

    return <div className='flex'>
        <Nav tabs>
            {tabs.map((tab, key) => <NavTab name={tab}
                                            key={key}
                                            activeTab={activeTab}
                                            toggle={toggle}/>)}
        </Nav>
        <TabContent activeTab={activeTab}>
            {tabs.map((tab, key) => <TabPane tabId={tab} key={key}>
                <GroupItems items={tabElements[tab]} {...props}/>
            </TabPane>)}
        </TabContent>
    </div>
};

ElementTabs.propTypes = {
    tabs: PropTypes.array.isRequired,
    elements: PropTypes.arrayOf(PropTypes.object).isRequired,
    elementsFieldTab: PropTypes.string.isRequired
};

export default ElementTabs;