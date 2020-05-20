import React from "react";

import ContainerColumn from "forms/utils/components/Container/ContainerColumn";

import CommonInfo from "./CommonInfo";
import Profile from "./Profile";

const MainInfo = () => {
    return <ContainerColumn>
        <CommonInfo/>
        <Profile/>
    </ContainerColumn>
};

export default MainInfo;