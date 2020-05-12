import React from "react";
import CommonInfo from "./CommonInfo";
import Profile from "./Profile";
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";

const MainInfo = () => {
    return <ContainerColumn>
        <CommonInfo/>
        <Profile/>
    </ContainerColumn>
};

export default MainInfo;