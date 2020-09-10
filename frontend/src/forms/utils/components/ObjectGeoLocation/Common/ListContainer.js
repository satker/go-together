import React from "react";
import RightContainer from "forms/utils/components/Container/RightContainer";

const ListContainer = ({children, height}) =>
    <RightContainer isBordered={true} style={{width: '30%', height}}>
        {children}
    </RightContainer>;

export default ListContainer;