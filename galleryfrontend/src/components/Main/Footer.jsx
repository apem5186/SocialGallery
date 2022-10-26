import footerData from "../../Data/footerData"
import { useState } from "react"
import { useSelector } from 'react-redux';

function Footer(){
    const [footer,setFooter] = useState(footerData)

    return(
        <>
            <div className="side-menu__footer">
                <div className="side-menu__footer_links">
                    <ul className="side-menu__footer_list">
                        <li className="side-menu__footer_item">
                            <a className="side-menu__footer_link" href="#">{
                                footer.map((a,i)=>{
                                    return(

                                        <span  key={a.id} >{a.text} &nbsp;</span>
                                    )
                                })
                            }</a>
                        </li>
                    </ul>
                </div>
                <span className="side-menu__footer_copyright">
            &copy; 2022 Social Gallery
        </span>
            </div>
        </>
    )
}
export default Footer