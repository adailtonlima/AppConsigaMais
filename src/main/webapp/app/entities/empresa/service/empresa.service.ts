import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmpresa, getEmpresaIdentifier } from '../empresa.model';

export type EntityResponseType = HttpResponse<IEmpresa>;
export type EntityArrayResponseType = HttpResponse<IEmpresa[]>;

@Injectable({ providedIn: 'root' })
export class EmpresaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/empresas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(empresa: IEmpresa): Observable<EntityResponseType> {
    return this.http.post<IEmpresa>(this.resourceUrl, empresa, { observe: 'response' });
  }

  update(empresa: IEmpresa): Observable<EntityResponseType> {
    return this.http.put<IEmpresa>(`${this.resourceUrl}/${getEmpresaIdentifier(empresa) as number}`, empresa, { observe: 'response' });
  }

  partialUpdate(empresa: IEmpresa): Observable<EntityResponseType> {
    return this.http.patch<IEmpresa>(`${this.resourceUrl}/${getEmpresaIdentifier(empresa) as number}`, empresa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmpresa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmpresa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEmpresaToCollectionIfMissing(empresaCollection: IEmpresa[], ...empresasToCheck: (IEmpresa | null | undefined)[]): IEmpresa[] {
    const empresas: IEmpresa[] = empresasToCheck.filter(isPresent);
    if (empresas.length > 0) {
      const empresaCollectionIdentifiers = empresaCollection.map(empresaItem => getEmpresaIdentifier(empresaItem)!);
      const empresasToAdd = empresas.filter(empresaItem => {
        const empresaIdentifier = getEmpresaIdentifier(empresaItem);
        if (empresaIdentifier == null || empresaCollectionIdentifiers.includes(empresaIdentifier)) {
          return false;
        }
        empresaCollectionIdentifiers.push(empresaIdentifier);
        return true;
      });
      return [...empresasToAdd, ...empresaCollection];
    }
    return empresaCollection;
  }
}
