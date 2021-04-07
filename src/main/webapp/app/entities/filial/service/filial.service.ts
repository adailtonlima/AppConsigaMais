import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFilial, getFilialIdentifier } from '../filial.model';

export type EntityResponseType = HttpResponse<IFilial>;
export type EntityArrayResponseType = HttpResponse<IFilial[]>;

@Injectable({ providedIn: 'root' })
export class FilialService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/filials');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(filial: IFilial): Observable<EntityResponseType> {
    return this.http.post<IFilial>(this.resourceUrl, filial, { observe: 'response' });
  }

  update(filial: IFilial): Observable<EntityResponseType> {
    return this.http.put<IFilial>(`${this.resourceUrl}/${getFilialIdentifier(filial) as number}`, filial, { observe: 'response' });
  }

  partialUpdate(filial: IFilial): Observable<EntityResponseType> {
    return this.http.patch<IFilial>(`${this.resourceUrl}/${getFilialIdentifier(filial) as number}`, filial, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFilial>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFilial[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFilialToCollectionIfMissing(filialCollection: IFilial[], ...filialsToCheck: (IFilial | null | undefined)[]): IFilial[] {
    const filials: IFilial[] = filialsToCheck.filter(isPresent);
    if (filials.length > 0) {
      const filialCollectionIdentifiers = filialCollection.map(filialItem => getFilialIdentifier(filialItem)!);
      const filialsToAdd = filials.filter(filialItem => {
        const filialIdentifier = getFilialIdentifier(filialItem);
        if (filialIdentifier == null || filialCollectionIdentifiers.includes(filialIdentifier)) {
          return false;
        }
        filialCollectionIdentifiers.push(filialIdentifier);
        return true;
      });
      return [...filialsToAdd, ...filialCollection];
    }
    return filialCollection;
  }
}
