import { Observable } from "rxjs";
import { BaseResponse, NgParam, PagesRequest } from "../model/model";

export interface BaseService<T> {
    searchSlect2(
        turn: string|null,
        customparam:NgParam[]
    ): any 
}