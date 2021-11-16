/**
 * Service Tasks application service.
 * 
 * @author Giulio Montagner
 * @version 1.0.0
 * @since 31st October 2017
 * @export 
 * @class ServiceTasksService
 */
import { BaseService } from '@myp3/foundation/service/BaseService';
import { IAugmentedEntityReply } from '@myp3/foundation/service/decorators/IAugmentedEntityReply';
import { SessionAwareRequest } from '@myp3/my-id/SessionAwareRequest';
import { ServiceError } from '@myp3/foundation/service/errors/ServiceError';
import { Inject, Provide } from '@myp3/foundation/ioc/Provide';
import { Logger } from '@myp3/foundation/logging/Logger';
import { ConfigurationService } from '@myp3/foundation/configuration/ConfigurationService';
import { FileManagementService } from './documents/FileManagementService';
import { IpaConfigHelper } from '@myp3/my-config-client/IpaConfigHelper';
import { WorkflowServiceAcl } from './WorkflowService';
import { UserDocumentsUtils } from './documents/UserDocumentsUtils';
import { MyPaClient } from './mypa/MyPaClient';
import { CommunicationSender, ContentShareableAttribute } from './../cms/mychannel/CommunicationSender';
import { BpmEngineClient } from '@myp3/my-flow';
import { ISessionUser } from '@myp3/my-id/model/ISessionUser';
import { ConfigHelper } from '@myp3/my-config-client/ConfigHelper';
import { BoxRepository } from '@myp3/my-box-client/BoxRepository';
import { BoxRepositoryFactory } from '@myp3/my-box-client/BoxRepositoryFactory';
import { Registry } from '@myp3/my-registry-client/Registry';
import mustache = require('mustache');
import * as Hapi from 'hapi';
import * as Q from 'q';
import { StandardProtocolClient } from './protocol/StandardProtocolClient';
import { CalendarServiceUtils } from '../calendar/CalendarServiceUtils';
import { EventRepository } from "../../data/EventRepository";
import { ResourceRepository, ResourceDocument } from "../../data/ResourceRepository";
import { MessagePusher } from "@myp3/my-transfer-client/MessagePusher";
import { ResourceTypologyRepository } from "../../data/ResourceTypologyRepository";
import * as req from 'request';
import * as stream from 'stream';
import {Channel} from '@myp3/my-channel-client/model/model';

export const GeneratePDFTaskName = 'Generate PDF';
export const GeneratePDFTemplateAttributeName = 'reportTemplateRef'
export const CustomServiceTaskName = 'Custom Service';
export const ChangeDocumentsVisibility = 'Change Documents Visibility';
export const SendMail = 'Send Mail';
export const FamilyUnitCertificateServiceTaskName = 'Family Unit Registry Certificate Service';
export const RegistryOfficeCertificateServiceTaskName = 'Registry Office Certificates Service.';
export const CreateEventName = 'Create Event';
export const ConfirmEventName = 'Confirm or decline Event';
export const RemoveEventName = 'Remove Event';

const CONFIRMED_EVENT: string = 'CONFIRMED';
const DECLINE_EVENT: string = 'REJECTED';
const PENDING_EVENT: string = 'PENDING';

interface RegistryId {
    registryId: string;
    istanzaOperatore: boolean;
}

interface WorkflowStatusObject {
    [key: string]: {
        value: string,
        disabled: boolean
    }
}

@Provide(ServiceTasksService)
export class ServiceTasksService extends BaseService {

    /**
     * 
     * 
     * @readonly
     * @private
     * @type {Model.ServiceTaskDescriptor[]}
     * @memberof ServiceTasksService
     */
    private get serviceDescriptors(): Model.ServiceTaskDescriptor[] {
        return [
            {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/savefile',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'content',
                            description: 'Variable containing Base64 encoded file content',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'fileName',
                            description: 'File name',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }, {
                            name: 'fileMimeType',
                            description: 'File MIME type',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }, {
                            name: 'isVisibleToUser',
                            description: 'Show within user files in MyPA',
                            isUserInput: true,
                            type: 'visibilitySelect',
                            isRequired: true
                        }
                    ],
                    output: [],
                },
                info: {
                    name: 'Save file',
                    description: 'Upload a file to user repository'
                }
            },
            {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/email',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'emailSubject',
                            description: 'Email Subject',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }, {
                            name: 'emailBody',
                            description: 'Email Body',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }, {
                            name: 'emailAddresses',
                            description: 'Addresses',
                            isUserInput: true,
                            type: 'templateVariables',
                            isRequired: true
                        }
                    ],
                    output: [
                        {
                            name: 'mailSent',
                            key: 'mailSent',
                            description: 'Stato invio mail',
                            processVariableName: 'mailSent',
                            type: 'string'
                        }
                    ]
                },
                info: {
                    name: 'Email Service',
                    description: 'Servizio di invio Mail'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/protocol',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }
                    ],
                    output: [
                        {
                            name: 'protocolNumber',
                            key: 'protocolNumber',
                            description: 'Numero di protocollo',
                            processVariableName: 'protocolNumber',
                            type: 'number'
                        }
                    ]
                },
                info: {
                    name: 'Example Service',
                    description: 'This service works as an example'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/status',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'statusKey',
                            description: 'Key of the status',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }, {
                            name: 'statusLabel',
                            description: 'Label of the status (shown to the user)',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }
                    ],
                    output: [
                        {
                            name: 'statusKey',
                            key: 'statusKey',
                            description: 'Stato assegnato',
                            processVariableName: 'statusKey',
                            type: 'string'
                        },
                        {
                            name: 'status',
                            key: 'status',
                            description: 'Nuovo stato',
                            processVariableName: 'status',
                            type: 'string'
                        }
                    ]
                },
                info: {
                    name: 'Update Status Service',
                    description: 'Updates the status of an instance'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/send-data',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        },
                        {
                            name: 'variables',
                            description: 'Variabili che verranno inviate al servizio di raccolta dati',
                            isUserInput: true,
                            type: 'templateVariables',
                            isRequired: true
                        },
                        {
                            name: 'url',
                            description: 'Url al quale verrano inviati i dati',
                            isUserInput: true,
                            type: 'textArea',
                            isRequired: false
                        }
                    ],
                    output: []
                },
                info: {
                    name: 'Send Service',
                    description: 'Servizio di invio dei dati'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/generate-pdf',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'istanzaOperatore',
                            value: 'istanzaOperatore',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processVersionId',
                            value: 'processVersionId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'taskId',
                            value: '{{taskId}}',
                            isUserInput: false,
                            type: 'string'
                        }, {
                            name: 'istanzaOperatore',
                            value: 'istanzaOperatore',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: GeneratePDFTemplateAttributeName,
                            description: 'Report template to use',
                            isUserInput: true,
                            type: 'templateRef',
                            isRequired: true
                        }, {
                            name: 'reportTemplateVariables',
                            description: 'Workflow variables that will be injected in the template',
                            isUserInput: true,
                            type: 'templateVariables',
                            isRequired: true
                        }, {
                            name: 'generatedFileName',
                            description: 'Name of the output file that will be generated',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }
                    ],
                    output: [{
                        name: 'reportPdfFile',
                        key: 'reportPdfFile',
                        description: '{{generatedFileName}}',
                        processVariableName: 'pdf_file_{{taskId}}',
                        type: 'file'
                    }]
                },
                info: {
                    name: GeneratePDFTaskName,
                    description: 'Generate PDF file from template'
                }
            },
            {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/event-book',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'resource',
                            description: 'Seleziona variabile relativa alla risorsa',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'title',
                            description: 'Seleziona variabile relativa al titolo',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'slots',
                            description: 'Seleziona variabile relativa agli slot di prenotazione',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'fiscalCode',
                            description: 'Seleziona variabile relativa al codice fiscale',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'practice',
                            description: 'Seleziona variabile relativa alla pratica',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }
                    ],
                    output: [{
                        name: 'eventIds',
                        key: 'eventIds',
                        description: 'Id degli eventi creati',
                        processVariableName: 'eventIds',
                        type: 'string'
                    }]
                },
                info: {
                    name: CreateEventName,
                    description: 'Create new event'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/event-approvation',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'eventIds',
                            description: 'Seleziona variabile relativa agli eventi',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        },
                        {
                            name: 'confirmed',
                            description: 'Seleziona variabile relativa alla conferma o rifiuto',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }
                    ],
                    output: []
                },
                info: {
                    name: ConfirmEventName,
                    description: 'Approve or decline an event'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/event-delete',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'eventIds',
                            description: 'Seleziona variabile relativa agli eventi',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }
                    ],
                    output: []
                },
                info: {
                    name: RemoveEventName,
                    description: 'Delete events flow'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/document-visibility',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processVersionId',
                            value: 'processVersionId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'taskId',
                            value: '{{taskId}}',
                            isUserInput: false,
                            type: 'string'
                        }, {
                            name: 'fileVariables',
                            description: 'Document that you want to change the visibility',
                            isUserInput: true,
                            type: 'fileVariables',
                            isRequired: true
                        }, {
                            name: 'visibilityValue',
                            description: 'Value of the visibility to set to the selected documents',
                            isUserInput: true,
                            type: 'visibilitySelect',
                            isRequired: true
                        }
                    ],
                    output: []
                },
                info: {
                    name: ChangeDocumentsVisibility,
                    description: 'Change visibility of documents'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/send-mail',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processVersionId',
                            value: 'processVersionId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'taskId',
                            value: '{{taskId}}',
                            isUserInput: false,
                            type: 'string'
                        }, {
                            name: 'type',
                            description: 'type of mail (email or pec)',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }, {
                            name: 'mailRecipients',
                            description: 'comma separated list of mail recipients',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        }, {
                            name: 'subject',
                            description: 'subject of the mail',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        },
                        {
                            name: 'body',
                            description: 'plain text of the mail body',
                            isUserInput: true,
                            type: 'textArea',
                            isRequired: true
                        }, {
                            name: 'fileVariables',
                            description: 'Documents that you want to put as attachments to the mail',
                            isUserInput: true,
                            type: 'fileVariablesAndReport',
                            isRequired: false
                        },
                        {
                            name: 'formsVariables',
                            description: 'Forms variables, theese are the variables that are replaced with ${{}}',
                            isUserInput: true,
                            type: 'templateVariables',
                            isRequired: false
                        }
                    ],
                    output: []
                },
                info: {
                    name: SendMail,
                    description: 'Send mail to multiple adresses'
                }
            }, {
                api: {
                    baseUrl: null,
                    endpoint: null,
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'url',
                            description: 'Script to generate url',
                            isUserInput: true,
                            type: 'textArea',
                            isRequired: true
                        },
                        {
                            name: 'method',
                            description: 'Http method for the call',
                            isUserInput: true,
                            type: 'string',
                            isRequired: true
                        },
                        {
                            name: 'headers',
                            description: 'Script to generate headers',
                            isUserInput: true,
                            type: 'textArea',
                            isRequired: false
                        },
                        {
                            name: 'payload',
                            description: 'Script to generate payload',
                            isUserInput: true,
                            type: 'textArea',
                            isRequired: false
                        },
                        {
                            name: 'output',
                            description: 'Script to parse output',
                            isUserInput: true,
                            type: 'textArea',
                            isRequired: false
                        }
                    ],
                    output: []
                },
                info: {
                    name: CustomServiceTaskName,
                    description: 'This is a custom service task. Exercise caution. Variables configured here won\'t be available in the model.'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.mylegacy.MyLegacyDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }
                    ],
                    output: [
                        {
                            name: 'protocolNumber',
                            key: 'protocolNumber',
                            description: 'Numero di protocollo',
                            processVariableName: 'protocolNumber',
                            type: 'number'
                        }
                    ]
                },
                info: {
                    name: 'Test Delegate',
                    description: 'Service Task di test'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.mylegacy.ProtocolloEstIntDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }
                    ],
                    output: [
                        {
                            name: 'protocolNumber',
                            key: 'protocolNumber',
                            description: 'Numero di protocollo',
                            processVariableName: 'protocolNumber',
                            type: 'number'
                        }
                    ]
                },
                info: {
                    name: 'Protocol Service Est/Int',
                    description: 'Protocol Service description'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.mylegacy.ProtocolloIntIntDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }
                    ],
                    output: [
                        {
                            name: 'protocolNumber',
                            key: 'protocolNumber',
                            description: 'Numero di protocollo',
                            processVariableName: 'protocolNumber',
                            type: 'number'
                        }
                    ]
                },
                info: {
                    name: 'Protocol Service Int/Int',
                    description: 'Protocol Service description Int/Int'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.mylegacy.ProtocolloIntEstDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }
                    ],
                    output: [
                        {
                            name: 'protocolNumber',
                            key: 'protocolNumber',
                            description: 'Numero di protocollo',
                            processVariableName: 'protocolNumber',
                            type: 'number'
                        }
                    ]
                },
                info: {
                    name: 'Protocol Service Int/Est',
                    description: 'Protocol Service description Int/Est'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.mylegacy.AnagrafeNucleoDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }
                    ],
                    output: []
                },
                info: {
                    name: FamilyUnitCertificateServiceTaskName,
                    description: 'Service that retrieves family unit certificate.'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.mylegacy.AnagrafeCertificatiDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }
                    ],
                    output: []
                },
                info: {
                    name: RegistryOfficeCertificateServiceTaskName,
                    description: 'Service that retrieves registry office certificates.'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.mylegacy.AnagrafeAggiornaPraticaDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [],
                    output: []
                },
                info: {
                    name: 'Aggiorna Pratica Service',
                    description: 'Aggiorna Pratica Service description'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.mylegacy.AnagrafeCambioResidenzaDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [],
                    output: []
                },
                info: {
                    name: 'Registra Avvio Pratica Service',
                    description: 'Registra Avvio Pratica Service description'
                }
            },
            {
                api: {
                    baseUrl: null,
                    endpoint: '/instances/service-task/workflow-status',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'workflowStatus',
                            value: '',
                            isUserInput: true,
                            type: 'keyValueGenerator'
                        }, {
                            name: 'statusMap',
                            value: 'statusMap',
                            isUserInput: false,
                            type: 'variable'
                        }
                    ],
                    output: [
                        {
                            name: 'statusMap',
                            key: 'statusMap',
                            description: 'Mappa degli stati (JSON)',
                            processVariableName: 'statusMap',
                            type: 'string'
                        }
                    ]
                },
                info: {
                    name: 'Update Workflow Status Variables',
                    description: 'Updates the status variables of an instance'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.myp3.ostacolialvolo.tasks.delegate.ImpiantoDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'st_tipo_operazione',
                            description: 'Operation type requested',
                            isUserInput: true,
                            type: 'keyValueList',
                            value: 'INS',
                            isRequired: true,
                            mapValues: [
                                {
                                    key: 'INS',
                                    value: 'Inserimento / Modifica',
                                },
                                {
                                    key: 'GET',
                                    value: 'Recupero dati impianto',
                                },
                                {
                                    key: 'VAL',
                                    value: 'Validazione'
                                },
                                {
                                    key: 'CLOSE',
                                    value: 'Chiusura'
                                },
                                {
                                    key: 'DEL',
                                    value: 'Eliminazione impianto'
                                },
                                {
                                    key: 'DEL_ROW',
                                    value: 'Cancellazione riga provvisoria'
                                },
                                {
                                    key: 'CARTOG_IMP',
                                    value: 'Salvataggio esito IDT'
                                },

                            ]

                        },
                        {
                            name: 'st_instanceid_operazione_in_corso',
                            description: 'Operation instance id',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS', 'VAL', 'CLOSE', 'DEL', 'DEL_ROW', 'CARTOG_IMP']
                            }
                        },
                        {
                            name: 'st_tipo_operazione_in_corso',
                            description: 'Operation type in progress',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS', 'VAL', 'CLOSE', 'DEL', 'DEL_ROW', 'CARTOG_IMP']
                            }
                        },
                        {
                            name: 'st_id_impianto',
                            description: 'Obstacle ID',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS', 'GET', 'VAL', 'CLOSE', 'DEL', 'DEL_ROW', 'CARTOG_IMP']
                            }
                        },
                        {
                            name: 'st_flg_dati_impianto_caricati',
                            description: 'Flag obstacle data loaded',
                            isUserInput: true,
                            type: 'keyValueList',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['VAL', 'CLOSE', 'DEL', 'DEL_ROW', 'CARTOG_IMP']
                            },
                            mapValues: [
                                {
                                    key: 'true',
                                    value: 'true',
                                },
                                {
                                    key: 'false',
                                    value: 'false',
                                },
                            ]
                        },
                        {
                            name: 'st_impianto_cod_ente',
                            description: 'Obstacle ente code',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_tipo_ostacolo_1l',
                            description: 'Obstacle category',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_tipo_ostacolo_2l',
                            description: 'Obstacle subcategory',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_tipo_ostacolo_3l',
                            description: 'Obstacle type',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_tipo_ostacolo_4l',
                            description: 'Obstacle subtype',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_censimento',
                            description: 'Census code',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_altezza',
                            description: 'Height code',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_segnalazione',
                            description: 'Reporting code',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_regionale',
                            description: 'Regional code',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_denominazione',
                            description: 'Obstacle name',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_flg_sgancio_temp',
                            description: 'Obstacle release flag',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_ubicazione_particolare',
                            description: 'Obstacle particular location',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_corografia_impianto',
                            description: 'Chorography with the layout of the obstacle',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_profilo_longitudine',
                            description: 'Longitudinal profile of the obstacle',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_tipo_ostacolo_altra_desc',
                            description: 'Obstacle type other description',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_lunghezza_incl',
                            description: 'Obstacle length inclined',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_provincia_1',
                            description: 'Obstacle province code (main)',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_provincia_2',
                            description: 'Obstacle province code 2',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_provincia_3',
                            description: 'Obstacle province code 3',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_provincia_4',
                            description: 'Obstacle province code 4',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cod_provincia_5',
                            description: 'Obstacle province code 5',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_data_inserimento',
                            description: 'Obstacle insert date',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_data_validazione',
                            description: 'Obstacle validation date',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_data_inizio_costruzione',
                            description: 'Obstacle start construction date',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_data_completamento_prev',
                            description: 'Obstacle expected completion date',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_data_scadenza_rimoz_prev',
                            description: 'Obstacle expected removal date',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_data_dismissione',
                            description: 'Obstacle disposal date',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_data_eliminazione',
                            description: 'Obstacle elimination date',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_note',
                            description: 'Obstacle note',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_impianto_elementi',
                            description: 'Obstacle elements',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_impianto_attraversamenti',
                            description: 'Obstacle crossings',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_id_gestore',
                            description: 'Obstacle manager id',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_tipo',
                            description: 'Obstacle manager type',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_flg_modifica',
                            description: 'Obstacle manager edit flag',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_flg_aggiungi',
                            description: 'Obstacle manager add flag',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_cod_fiscale',
                            description: 'Obstacle manager fiscal code',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_partita_iva',
                            description: 'Obstacle manager VAT number',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_nome',
                            description: 'Obstacle manager name',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_cognome',
                            description: 'Obstacle manager surname',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_genere',
                            description: 'Obstacle manager gender',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_denominazione',
                            description: 'Obstacle manager denomination',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_telefono',
                            description: 'Obstacle manager phone',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_email',
                            description: 'Obstacle manager email',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_pec',
                            description: 'Obstacle manager pec',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_nascita_cod_stato',
                            description: 'Obstacle manager birth state',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_nascita_cod_provincia',
                            description: 'Obstacle manager birth province',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_nascita_cod_comune',
                            description: 'Obstacle manager birth municipality',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_nascita_data',
                            description: 'Obstacle manager birth date',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_indirizzo_cod_stato',
                            description: 'Obstacle manager address state',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_indirizzo_cod_provincia',
                            description: 'Obstacle manager address province',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_indirizzo_cod_comune',
                            description: 'Obstacle manager address municipality',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_indirizzo_cap',
                            description: 'Obstacle manager address postal code',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_indirizzo_via',
                            description: 'Obstacle manager address street',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_gestore_indirizzo_numero',
                            description: 'Obstacle manager address number',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['INS']
                            }
                        },
                        {
                            name: 'st_cartog_allinea_esito',
                            description: 'IDT alingment result',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['CARTOG_IMP']
                            }
                        },
                        {
                            name: 'st_cartog_allinea_msg',
                            description: 'IDT alingment message',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['CARTOG_IMP']
                            }
                        },
                        {
                            name: 'st_cartog_allinea_id',
                            description: 'IDT alingment id',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_tipo_operazione',
                                values: ['CARTOG_IMP']
                            }
                        },

                    ],
                    output: [],
                    saveAsBinary: true
                },
                info: {
                    name: 'Operazioni per Ostacoli al Volo',
                    description: 'Task per operazioni su Ostacoli al Volo'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|it.regioneveneto.myp3.idt.tasks.delegate.IdtDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'st_idt_tipo_operazione',
                            description: 'Operation type requested',
                            isUserInput: true,
                            type: 'keyValueList',
                            value: 'CREA',
                            isRequired: true,
                            mapValues: [
                                {
                                    key: 'CREA',
                                    value: 'Creazione',
                                },
                                {
                                    key: 'CANC',
                                    value: 'Cancellazione',
                                }
                            ]

                        },
                        {
                            name: 'st_idt_outcome_status',
                            description: 'Select variable in witch outcome flag is stored',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA', 'CANC']
                            }
                        }, 
                        {
                            name: 'st_idt_error_msg',
                            description: 'Select variable in witch error message is stored',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA', 'CANC']
                            }
                        }, 
                        {
                            name: 'st_idt_feature_outcome_id',
                            description: 'Select variable in witch outcome feature id is stored',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA', 'CANC']
                            }
                        }, 
                        {
                            name: 'st_idt_layer',
                            description: 'Idt Layer',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA', 'CANC']
                            }
                        },
                        {
                            name: 'st_idt_type',
                            description: 'Idt type',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        }, 
                        {
                            name: 'st_idt_id',
                            description: 'Idt system id',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_ele',
                            description: 'Idt max hight',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_time',
                            description: 'Idt time',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_name',
                            description: 'Idt Name',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_desc',
                            description: 'Idt description',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_simbologia',
                            description: 'Idt simbologia',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_ele_string',
                            description: 'Idt array of points height as string',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_geometry_type',
                            description: 'Idt geometry type',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_geometry_coordinates',
                            description: 'Idt geometry coordinates as Array of points',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CREA']
                            }
                        },
                        {
                            name: 'st_idt_id_feature',
                            description: 'Idt id feature',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: false,
                            dependingOn: {
                                key: 'st_idt_tipo_operazione',
                                values: ['CANC']
                            }
                        },
                    ],
                    output: [],
                    saveAsBinary: true
                },
                info: {
                    name: 'Operazioni su IDT',
                    description: 'Task per esecuzione di operazioni su IDT'
                }
            },
            {
                api: {
                    baseUrl: 'JAVA',
                    endpoint: 'JAVA|ms.camunda.taskservizi.BandiDelegate',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json'
                    },
                    payload: [
                        {
                            name: 'userRegistryId',
                            value: 'userRegistryId',
                            isUserInput: false,
                            type: 'variable'
                        }, {
                            name: 'processInstanceId',
                            value: 'processInstanceId',
                            isUserInput: false,
                            type: 'variable'
                        },
                        {
                            name: 'id_bandi',
                            description: 'identificativo bando',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                            
                        }, {
                            name: 'nome_richiedente',
                            description: 'nome rihiedente',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                            
                        }, {
                            name: 'cognome_richiedente',
                            description: 'cognome richiedente',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'codice_fiscale_richiedente',
                            description: 'codice fiscale richiedente',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'data_fine_bando',
                            description: 'Data fine bando',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                            
                        }, {
                            name: 'titolo_richiedente',
                            description: 'titolo richiedente',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'numero_di_telefono_richiedente',
                            description: 'telefono richiedente',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'indirizzo_posta_elettronica_richiedente',
                            description: 'email richiedente',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'codice_fiscale_beneficiario',
                            description: 'codice fiscale beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'numero_di_telefono_beneficiario',
                            description: 'telefono beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'indirizzo_posta_elettronica_beneficiario',
                            description: 'email beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'nome_beneficiario',
                            description: 'nome beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'cognome_beneficiario',
                            description: 'cognome beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'comune_residenza_beneficiario',
                            description: 'comune residenza beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'cap_residenza_beneficiario',
                            description: 'cap residenza beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'indirizzo_residenza_beneficiario',
                            description: 'indirizzo residenza beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'cittadinanza_beneficiario',
                            description: 'cittadinanza beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'num_componenti_nucleo',
                            description: 'componenti nucleo familiare beneficiario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'informazioni_sulla_condizione_economica_reddito',
                            description: 'reddito',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'informazioni_patrimoniale_mobiliare',
                            description: 'patrimonio mobiliare',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'informazioni_patrimoniale_immobiliare',
                            description: 'patrimonio immobiliare',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'prestazioni_sociali_erogate_ai_componenti_del_nucleo_familiare',
                            description: 'prestazioni erogate al nucleo familiare',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'residente_nel_comune_di_riferimento',
                            description: 'residente nel comune di riferimento',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'cittadino_italiano_o_di_un_paese_unione_europea',
                            description: 'cittadino italiano o di un paese appartenente alla UE',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'cittadino_straniero_titolare_di_permesso',
                            description: 'cittadino straniero con permesso di soggiorno',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'codice_iban',
                            description: 'codice iban',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'intestatario',
                            description: 'intestatario',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'istituto_di_credito',
                            description: 'istituto di credito',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'list_array_nucleo_familiare',
                            description: 'nucleo_familiare',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'numero_protocollo_manuale',
                            description: 'protocollazione in entrata',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        }, {
                            name: 'codice_fiscale_profilo',
                            description: 'codice_fiscale_profilo',
                            isUserInput: true,
                            type: 'singleVariable',
                            isRequired: true
                        },
                    ],
                    output: [],
                },
                info: {
                    name: 'Contributo Economico',
                    description: 'Contributo Economico'
                }
            }
        ];
    }


    /**
     * The message pusher
     * 
     * @private
     * @type {MessagePusher}
     * @memberOf PollService
     */
    private messagePusher: MessagePusher;


    /**
     *
     *
     * @private
     * @type {BoxRepository}
     * @memberof ServiceTasksService
     */
    private boxRepository: BoxRepository;

    /**
     * Creates an instance of ServiceTasksService.
     * 
     * @param {Logger} logger
     * @param {ConfigurationService} configurationService
     * 
     * @memberOf ServiceTasksService
     */
    constructor(
        @Inject(Logger) private logger: Logger,
        @Inject(ConfigurationService) private configurationService: ConfigurationService,
        @Inject(FileManagementService) private fileManagementService: FileManagementService,
        @Inject(IpaConfigHelper) private ipaConfigHelper: IpaConfigHelper,
        @Inject(UserDocumentsUtils) private userDocsUtils: UserDocumentsUtils,
        @Inject(MyPaClient) private myPaClient: MyPaClient,
        @Inject(CommunicationSender) private communicationSender: CommunicationSender,
        @Inject(ConfigHelper) private configHelper: ConfigHelper,
        @Inject(BpmEngineClient) private bpmEngineClient: BpmEngineClient,
        @Inject(EventRepository) private eventRepository: EventRepository,
        @Inject(ResourceRepository) private resourceRepository: ResourceRepository,
        @Inject(ResourceTypologyRepository) private typologyRepository: ResourceTypologyRepository,
        @Inject(CalendarServiceUtils) private calendarUtils: CalendarServiceUtils,
        @Inject(BoxRepositoryFactory) private boxRepositoryFactory: BoxRepositoryFactory    ) {
        super();

        this.boxRepository = this.boxRepositoryFactory.getInstance();

        this.name = 'ServiceTasksService';
        this.version = '1.0.0';
        this.messagePusher = new MessagePusher(
            this.configurationService.get<string>('mytransfer.service.baseUrl'),
            this.configurationService.get<string>('mytransfer.messageKeySuffix'));
    }

    /**
     * 
     * 
     * @protected
     * @returns {Array<Hapi.IRouteConfiguration>}
     * 
     * @memberOf ServiceTasksService
     */
    protected getHandlers(): Array<Hapi.IRouteConfiguration> {
        return new Array<Hapi.IRouteConfiguration>(
            {
                method: 'GET',
                path: '/myintranet/{ipa}/instances/service-tasks',
                handler: (request, response) => {
                    this.getServiceTasks(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: 'default',
                    description: 'Get all available service tasks',
                    notes: 'myintranet - get all available service tasks',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        aclEnforcer: {
                            acl: WorkflowServiceAcl
                        }
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/email',
                handler: (request, response) => {
                    this.sendEmail(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Send email to receivers',
                    notes: 'myintranet - send email to receivers',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            userRegistryId: this.extendedJoi.string().required(),
                            processInstanceId: this.extendedJoi.string().required(),
                            emailSubject: this.extendedJoi.string().required(),
                            emailBody: this.extendedJoi.string().required(),
                            emailAddresses: this.extendedJoi.object().required()
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/status',
                handler: (request, response) => {
                    this.updateInstanceStatus(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Update status for process',
                    notes: 'myintranet - update status for process',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            userRegistryId: this.extendedJoi.string().required(),
                            processInstanceId: this.extendedJoi.string().required(),
                            statusKey: this.extendedJoi.string().required(),
                            statusLabel: this.extendedJoi.string().required()
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/send-data',
                handler: (request, response) => {
                    this.sendData(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'send-data',
                    notes: 'myintranet - send-data',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            userRegistryId: this.extendedJoi.string().required(),
                            processInstanceId: this.extendedJoi.string().required(),
                            variables: this.extendedJoi.object().required(),
                            url: this.extendedJoi.string().optional()
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/savefile',
                handler: (request, response) => {
                    this.saveFile(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Save a file to user repository',
                    notes: 'myintranet - save a file to user repository',
                    tags: ['api'],
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            userRegistryId: this.extendedJoi.string().required(),
                            processInstanceId: this.extendedJoi.string().required(),
                            content: this.extendedJoi.object().required(),
                            fileName: this.extendedJoi.string().required(),
                            fileMimeType: this.extendedJoi.string().required(),
                            isVisibleToUser: this.extendedJoi.boolean().required()
                        }
                    },
                    plugins: {
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            },
            {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/generate-pdf',
                handler: (request, response) => {
                    this.generatePdfForProcess(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Generate PDF file from template',
                    notes: 'myintranet - generate PDF file from template',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            userRegistryId: this.extendedJoi.string().required(),
                            processInstanceId: this.extendedJoi.string().required(),
                            taskId: this.extendedJoi.string().required(),
                            reportTemplateRef: this.extendedJoi.string().required(),
                            generatedFileName: this.extendedJoi.string().required(),
                            processVersionId: this.extendedJoi.string().required(),
                            reportTemplateVariables: this.extendedJoi.object().required(),
                            istanzaOperatore: this.extendedJoi.boolean().optional().default(false)
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            },
            {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/workflow-status',
                handler: (request, response) => {
                    this.updateWorkflowStatus(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Update workflow status for process',
                    notes: 'myintranet - update workflow status for process',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            userRegistryId: this.extendedJoi.string().required(),
                            processInstanceId: this.extendedJoi.string().required(),
                            workflowStatus: this.extendedJoi.string().required(),
                            statusMap: this.extendedJoi.string().optional().allow(null)
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/event-book',
                handler: (request, response) => {
                    this.bookEvent(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Create new Event',
                    notes: 'myintranet - Create new event',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            resource: this.extendedJoi.object().required(),
                            title: this.extendedJoi.object().required(),
                            slots: this.extendedJoi.object().required(),
                            fiscalCode: this.extendedJoi.object().optional(),
                            practice: this.extendedJoi.object().optional()
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/event-approvation',
                handler: (request, response) => {
                    this.confirmEvent(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Approve o decline an event',
                    notes: 'myintranet - Approve o decline an event',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            eventIds: this.extendedJoi.object().required(),
                            confirmed: this.extendedJoi.object().required()
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/event-delete',
                handler: (request, response) => {
                    this.deleteEvent(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Delete an event',
                    notes: 'myintranet - Delete an event',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            eventIds: this.extendedJoi.object().required()
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/document-visibility',
                handler: (request, response) => {
                    this.changeCitizenVisibilityDocuments(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Change visibility of documents for citizen',
                    notes: 'myintranet - Change visibility of documents for citizen',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            userRegistryId: this.extendedJoi.string().required(),
                            processInstanceId: this.extendedJoi.string().required(),
                            taskId: this.extendedJoi.string().required(),
                            processVersionId: this.extendedJoi.string().required(),
                            fileVariables: this.extendedJoi.object().required(),
                            visibilityValue: this.extendedJoi.boolean().required(),
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }, {
                method: 'POST',
                path: '/myintranet/{ipa}/instances/service-task/send-mail',
                handler: (request, response) => {
                    this.sendMail(<SessionAwareRequest>request, <IAugmentedEntityReply>response);
                },
                config: {
                    auth: false,
                    description: 'Change visibility of documents for citizen',
                    notes: 'myintranet - Change visibility of documents for citizen',
                    tags: ['api'],
                    cache: {
                        expiresIn: this.configurationService.get<number>('myintranet.cachingLevel.disabled'),
                        expiresAt: null,
                        privacy: 'private'
                    },
                    validate: {
                        params: {
                            ipa: this.extendedJoi.string().required().regex(/^(([A-Z])[\w-]+)$/gi)
                        },
                        payload: {
                            userRegistryId: this.extendedJoi.string().required(),
                            processInstanceId: this.extendedJoi.string().required(),
                            taskId: this.extendedJoi.string().required(),
                            processVersionId: this.extendedJoi.string().required(),
                            fileVariables: this.extendedJoi.string().optional().allow(null), //string to allow character *
                            mailRecipients: this.extendedJoi.string().required(),
                            subject: this.extendedJoi.string().required(),
                            body: this.extendedJoi.string().required(),
                            type: this.extendedJoi.string().required(),
                            formsVariables: this.extendedJoi.object().optional().allow(null)
                        }
                    },
                    plugins: {
                        cacheHeadersMapper: {
                            retention: 'internal'
                        },
                        ipaVerification: {
                            isEnabled: false
                        },
                        crumb: false
                    }
                }
            }

        );
    }

    /**
     * 
     * 
     * @param {SessionAwareRequest} request 
     * @param {IAugmentedEntityReply} reply 
     * 
     * @memberof ServiceTasksService
     * 
     * @swagger
     * definitions:
     *   ServiceResponseProtocol:
     *     allOf:
     *     - $ref: '#/definitions/ServiceResponse'
     *     - type: object
     *       required:
     *       - entity
     *       properties:
     *         entity:
     *           type: object
     *           required:
     *           - protocolNumber
     *           properties:
     *             protocolNumber:
     *               type: number
     * 
     * /myintranet/{ipa}/instances/service-task/protocol:
     *   post:
     *     tags:
     *     - 'ServiceTasks Service'
     *     description: Get protocol number for a process instance.
     *     produces:
     *       - application/json
     *     parameters:
     *       - name: ipa
     *         description: Portal's IPA.
     *         in: path
     *         required: true
     *         type: string
     *       - name: payload
     *         description: get protocol number payload
     *         in: body
     *         required: true
     *         schema:
     *            type: object
     *            required:
     *            - userRegistryId
     *            - processInstanceId
     *            properties:
     *              userRegistryId:
     *                type: string
     *              processInstanceId:
     *                type: string
     *     responses:
     *       200:
     *         description: protocol number
     *         schema:
     *           $ref: '#/definitions/ServiceResponseProtocol'
     *       500:
     *         description: Generic Error
     *         schema:
     *           $ref: '#/definitions/ServiceResponseError'
     */
    public getProtocolNumber(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const { userRegistryId, processInstanceId } = request.payload;
        const ipa = request.params['ipa'];
        const protocolNumber = Math.trunc(Math.random() * 10000);
        let istanzaOperatore : boolean = false;
        this.getUserRegistryId(processInstanceId)
        .then((id: RegistryId) => {                
            istanzaOperatore = id.istanzaOperatore;
            return this.fileManagementService.getProcessInstanceFiles(userRegistryId, ipa, processInstanceId, istanzaOperatore)
        })
            .then(result => {
                if (!result)
                    throw new ServiceError(`Cannot find user documents for process instance id = ${processInstanceId}`);

                return this.fileManagementService.updateFile(userRegistryId, processInstanceId, ipa, { protocolNumber });
            })
            .then(() => {
                reply.replyWithEntity({ protocolNumber });
            })
            .catch(error => {
                reply.replyWithError(error);
            });
    }

    /**
     * 
     * 
     * @param {SessionAwareRequest} request 
     * @param {IAugmentedEntityReply} reply 
     * 
     * @memberof ServiceTasksService
     * 
     * @swagger
     * definitions:
     *   ServiceResponseProtocol:
     *     allOf:
     *     - $ref: '#/definitions/ServiceResponse'
     *     - type: object
     *       required:
     *       - entity
     *       properties:
     *         entity:
     *           type: object
     *           required:
     *           - protocolNumber
     *           properties:
     *             protocolNumber:
     *               type: number
     * 
     * /myintranet/{ipa}/instances/service-task/email:
     *   post:
     *     tags:
     *     - 'ServiceTasks Service'
     *     description: Send Email to receivers
     *     produces:
     *       - application/json
     *     parameters:
     *       - name: ipa
     *         description: Portal's IPA.
     *         in: path
     *         required: true
     *         type: string
     *       - name: payload
     *         description:  Send Email to receivers
     *         in: body
     *         required: true
     *         schema:
     *            type: object
     *            required:
     *            - userRegistryId
     *            - processInstanceId
     *            - emailSubject
     *            - emailBody
     *            - emailAddresses
     *            properties:
     *              userRegistryId:
     *                type: string
     *              processInstanceId:
     *                type: string
     *              emailSubject:
     *                type: string
     *              emailBody:
     *                type: string
     *              emailAddresses:
     *                type: object
     *     responses:
     *       200:
     *         description: protocol number
     *         schema:
     *           $ref: '#/definitions/ServiceResponseProtocol'
     *       500:
     *         description: Generic Error
     *         schema:
     *           $ref: '#/definitions/ServiceResponseError'
     */
    public sendEmail(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const {
            emailSubject,
            emailBody,
            emailAddresses
        } = request.payload;
        const ipa = request.params['ipa'];


        const emailAddressesArray: string[] = [];
        Object.keys(emailAddresses)
            .forEach(k => emailAddressesArray.push(emailAddresses[k]));

        // Compose and send mail
        this.communicationSender.sendEmailToAddresses(
            ipa,
            emailSubject,
            emailBody,
            emailAddressesArray
        ).then((response) => {
            const communicationId: string = response.communicationId;
            const scheduleId: string = response.scheduleId;

            //TODO da sistemare la gestione dello stato di ritorno
            const mailSent: string = (communicationId && scheduleId) ? 'OK' : 'KO';
            reply.replyWithEntity({ mailSent: mailSent });
        })
            .catch(error => {
                return reply.replyWithError(error);
            });
    }


    /**
     * 
     * 
     * @author Giulio Montagner
     * @param {SessionAwareRequest} request 
     * @param {IAugmentedEntityReply} reply 
     * @memberof ServiceTasksService
     * 
     * @swagger
     * /myintranet/{ipa}/instances/service-tasks:
     *   get:
     *     tags:
     *     - 'ServiceTasks Service'
     *     description: Get all available service tasks.
     *     produces:
     *       - application/json
     *     parameters:
     *       - name: ipa
     *         description: Portal's IPA.
     *         in: path
     *         required: true
     *         type: string
     *     responses:
     *       200:
     *         description: generic
     *         schema:
     *           $ref: '#/definitions/ServiceResponse'
     *       500:
     *         description: Generic Error
     *         schema:
     *           $ref: '#/definitions/ServiceResponseError'
     */
    public getServiceTasks(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        this.ipaConfigHelper.getIpaConfiguration(request.session.getProfile().ipa)
            .then(configuration => {
                reply.replyWithEntities(
                    this.serviceDescriptors.map(service => {
                        service.api.baseUrl = `${configuration.myintranet.baseUrl.private}${configuration.myintranet.context}`;
                        return service;
                    })
                );
            })
            .catch(error => {
                reply.replyWithError(error);
            });
    }

    /**
     * 
     * 
     * @param {SessionAwareRequest} request 
     * @param {IAugmentedEntityReply} reply 
     * 
     * @memberof ServiceTasksService
     * 
     * @swagger 
     * /myintranet/{ipa}/instances/service-task/status:
     *   post:
     *     tags:
     *     - 'ServiceTasks Service'
     *     description: Update status for a process instance.
     *     produces:
     *       - application/json
     *     parameters:
     *       - name: ipa
     *         description: Portal's IPA.
     *         in: path
     *         required: true
     *         type: string
     *       - name: payload
     *         description: status update payload
     *         in: body
     *         required: true
     *         schema:
     *            type: object
     *            required:
     *            - userRegistryId
     *            - processInstanceId
     *            - statusKey
     *            - statusLabel
     *            properties:
     *              userRegistryId:
     *                type: string
     *              processInstanceId:
     *                type: string
     *              statusKey:
     *                type: string
     *              statusLabel:
     *                type: string
     *     responses:
     *       200:
     *         description: success status
     *         schema:
     *           $ref: '#/definitions/ServiceResponse'
     *       500:
     *         description: Generic Error
     *         schema:
     *           $ref: '#/definitions/ServiceResponseError'
     */
    public updateInstanceStatus(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const { processInstanceId } = request.payload;
        const ipa = request.params['ipa'];
        const newStatus: Model.InstanceUserFileStatus = {
            key: request.payload.statusKey,
            label: request.payload.statusLabel,
            startedAt: null,
            endedAt: null
        };

        this.logger.info('Updating instance status', {
            statusKey: newStatus.key,
            statusLabel: newStatus.label,
            ipa,
            processInstanceId
        });

        let userContacts: any = null;
        let variables: { [key: string]: Model.VariableValue<any> } = null;
        let userProfile: ISessionUser = null;

        let procedureName: string;
        let procedureNumber: string;
        let status: Array<any>;


        this.logger.info('Getting process instance variables', {
            processInstanceId
        });

        let registryId: string;
        let istanzaOperatore: boolean;

        this.getUserRegistryId(processInstanceId)
            .then((id: RegistryId) => {
                registryId = id.registryId;
                istanzaOperatore = id.istanzaOperatore;

                return this.bpmEngineClient.getProcessInstanceVariables(processInstanceId)
            })
            .then(results => {
                variables = results;
                userProfile = JSON.parse(variables['userProfile'].value);

                this.logger.info('Process instance variables have been retrieved. Getting instance files', {
                    registryId,
                    ipa,
                    processInstanceId
                });

                return this.fileManagementService.getProcessInstanceFiles(registryId, ipa, processInstanceId, istanzaOperatore);
            })
            .then(result => {
                if (!result)
                    throw new ServiceError(`Cannot find user documents for process instance id = ${processInstanceId}`);

                this.logger.info('Process instance files have been retrieved. Updating file to the new status', {
                    registryId,
                    ipa,
                    processInstanceId,
                    newStatus
                });

                procedureNumber = result.procedureNumber;
                procedureName = result.procedureName;
                status = result.status;
                return this.fileManagementService.updateFile(/*userRegistryId*/ registryId, processInstanceId, ipa, { status: newStatus }, newStatus, [], null, null, istanzaOperatore);
            })
            /*
            .then(() => {
                if (istanzaOperatore) {
                    // update status in camunda
                    const variables: {[key: string]: Model.VariableValue<any>} = {
                        status: { type: 'string', value: newStatus.key }
                    }
                    return this.bpmEngineClient.setProcessInstanceVariables(processInstanceId, variables);
                } else {
                    return Q.resolve<any>(null);
                }
            })
            */
            .then(() => {
                if (!istanzaOperatore) {
                    this.logger.info('File status has been updated. Sending notification to MyPA', {
                        registryId,
                        ipa,
                        processInstanceId,
                        newStatus
                    });

                    return this.myPaClient.notifyUserInstanceUpdate(/*userRegistryId*/ registryId,
                        ipa, 'La tua domanda è stata aggiornata', processInstanceId, procedureNumber, procedureName, status)
                } else {
                    return Q.resolve<any>(null);
                }
            })
            .then(() => {
                if (!istanzaOperatore) {
                    this.logger.info('Notification has been sent to MyPA. Getting user contacts for status update notification sending', {
                        registryId,
                        ipa,
                        processInstanceId
                    });

                    return this.myPaClient.getInstanceStatusUpdateUserContacts(/*userRegistryId*/ registryId);
                } else {
                    return Q.resolve<any>(null);
                }
            })
            .then(result => {
                if (!istanzaOperatore) {
                    if (!result.contactsPreferences.mail)
                        return null;

                    if (!result.userContacts.mail && !result.userContacts.secondaryMail)
                        return null;

                    userContacts = result;

                    this.logger.info('Building user notification', {
                        registryId,
                        ipa,
                        processInstanceId
                    });

                    return this.getStatusUpdateNotificationContents(ipa, variables, userProfile);
                } else {
                    return Q.resolve<any>(null);
                }
            })
            .then(results => {
                if (!istanzaOperatore) {
                    // Send communications
                    this.logger.info('User notification has been built. Sending', {
                        registryId,
                        ipa,
                        processInstanceId,
                        notificationContents: results
                    });

                    return Q.allSettled([userContacts ? userContacts.userContacts.mail : null, userContacts ? userContacts.userContacts.secondaryMail : null].filter(c => c).map(c => {
                        // Compose and send mail
                        return this.communicationSender.sendCommunicationToReceiver(
                            processInstanceId,
                            'notifica aggiornamento stato istanza',
                            'istanza',
                            ipa,
                            'email',
                            (userProfile && userProfile.firstName) || '',
                            (userProfile && userProfile.lastName) || '',
                            c,
                            results);
                    }));
                } else {
                    return Q.resolve<any>(null);
                }
            })
            .then(() => {
                this.logger.info('User notification has been sent.', {
                    registryId,
                    ipa,
                    processInstanceId,
                    value: `${newStatus.key}:${newStatus.label}`
                });

                return reply.replyWithEntity({
                    status: newStatus.key,
                    statusKey: `${newStatus.key}:${newStatus.label}`
                });
            })
            .catch(error => {
                this.logger.error('An error occurred while updating process instance status', {
                    message: error ? error.message : null,
                    stack: error ? error.stack : null
                });

                return reply.replyWithError(error);
            });
    }


    /**
     *
     * @author Michele Da Meda
     * @param {SessionAwareRequest} request
     * @param {IAugmentedEntityReply} reply
     * @memberof ServiceTasksService
     *
     * @swagger 
     * /myintranet/{ipa}/instances/service-task/workflow-status':
     *   post:
     *     tags:
     *     - 'ServiceTasks Service'
     *     description: Update workflow status (update a set of configured variables) for a process instance. workflowStatus is a json string
     *     produces:
     *       - application/json
     *     parameters:
     *       - name: ipa
     *         description: Portal's IPA.
     *         in: path
     *         required: true
     *         type: string
     *       - name: payload
     *         description: status update payload
     *         in: body
     *         required: true
     *         schema:
     *            type: object
     *            required:
     *            - userRegistryId
     *            - processInstanceId
     *            - workflowStatus
     *            properties:
     *              userRegistryId:
     *                type: string
     *              processInstanceId:
     *                type: string
     *              workflowStatus:
     *                type: string
     *     responses:
     *       200:
     *         description: success status
     *         schema:
     *           $ref: '#/definitions/ServiceResponse'
     *       500:
     *         description: Generic Error
     *         schema:
     *           $ref: '#/definitions/ServiceResponseError'
     */
    public updateWorkflowStatus(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const {
            workflowStatus,
            statusMap,
        } = request.payload;

        try {

            const status: WorkflowStatusObject = JSON.parse(workflowStatus);
            //this.bpmEngineClient.getProcessInstanceVariables(processInstanceId)

            const variables: { [key: string]: Model.VariableValue<any> } = (statusMap) ? JSON.parse(statusMap) : {};
            for (let param in status) {
                if (!status[param].disabled) {
                    variables[param] = { type: 'string', value: status[param].value };
                }
            }
            // da tornare come valore e non come set delle variabili di processo per non avere errori di transazioni concorrenti
            reply.replyWithEntity({
                statusMap: JSON.stringify(variables)
            });
        }
        catch (e) {
            this.logger.error(`Errore nell'aggiornamento della mappa degli stati sul workflow`, e);
            reply.replyWithError(e);
        }

    }

    /**
     *
     *
     * @param {SessionAwareRequest} request
     * @param {IAugmentedEntityReply} reply
     * @memberof ServiceTasksService
     */
    public sendData(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const { userRegistryId, processInstanceId, variables, url } = request.payload;
        const ipa = request.params['ipa'];
        const fileIds = [];
        let defaultUrl = '';
        let istanzaOperatore : boolean = false
        Q.Promise<any>((resolve, reject) => {
            if (url)
                return resolve(null);

            this.configHelper
                .getItem(`${ipa}>myp3>myintranet>send-data-url`)
                .then(result => {
                    defaultUrl = result.value[0];
                    resolve(null);
                })
                .catch(error => reject(error));
        }).then(() => {
           
        return this.getUserRegistryId(processInstanceId)
        
        }).then((id: RegistryId) => {                
            istanzaOperatore = id.istanzaOperatore;
            return this.fileManagementService
                .getProcessInstanceFiles(userRegistryId, ipa, processInstanceId, istanzaOperatore);
        })
            .then(result => {
                const userId = result.userId;

                result.contents.forEach(result => {
                    const filePointer = Object.keys(variables).find(k => {
                        return result.id === variables[k];
                    });

                    if (filePointer)
                        fileIds.push({
                            id: result.file.id,
                            variable: filePointer
                        });
                });

                if (fileIds.length === 0)
                    return [];

                return Q.all(fileIds.map(fid => {
                    return this.boxRepository
                        .get(userId, fid.id)
                        .then(result => this.toBuffer(result))
                        .then(result => {
                            return {
                                variable: fid.variable,
                                result
                            };
                        });
                }));
            })
            .then(results => {
                let formData = JSON.parse(JSON.stringify(variables));

                // Replace files in variables
                results.forEach(r => {
                    formData[r.variable] = r.result;
                });

                return Q.Promise<any>((resolve, reject) => {
                    req.post({
                        url: url || defaultUrl,
                        formData
                    }, (error, response) => {
                        if (error || (response && response.statusCode !== 200)) {
                            this.logger.error('An error occurred while sending data to external service', {
                                error: error ? error.message : null,
                                statusCode: response ? response.statusCode : null
                            });

                            return reject(new Error('Invalid response or error occurred'));
                        }

                        resolve(null);
                    });
                });
            })
            .then(() => reply.replyWithEntity({}))
            .catch(error => {
                reply.replyWithError(error);
            });
    }

    /**
     * 
     * 
     * @param {SessionAwareRequest} request 
     * @param {IAugmentedEntityReply} reply 
     * 
     * @memberof ServiceTasksService
     * 
     * @swagger
     * definitions:
     *   ServiceResponseGeneratePdf:
     *     allOf:
     *     - $ref: '#/definitions/ServiceResponse'
     *     - type: object
     *       required:
     *       - entity
     *       properties:
     *         entity:
     *           type: object
     *           required:
     *           - reportPdfFile
     *           properties:
     *             reportPdfFile:
     *               type: string
     * 
     * /myintranet/{ipa}/instances/service-task/generate-pdf:
     *   post:
     *     tags:
     *     - 'ServiceTasks Service'
     *     description: Generate PDF file from template.
     *     produces:
     *       - application/json
     *     parameters:
     *       - name: ipa
     *         description: Portal's IPA.
     *         in: path
     *         required: true
     *         type: string
     *       - name: payload
     *         description: get protocol number payload
     *         in: body
     *         required: true
     *         schema:
     *            type: object
     *            required:
     *            - userRegistryId
     *            - processInstanceId
     *            - reportTemplateRef
     *            - generatedFileName
     *            - taskId
     *            - processVersionId
     *            - reportTemplateVariables
     *            properties:
     *              userRegistryId:
     *                type: string
     *              processInstanceId:
     *                type: string
     *              reportTemplateRef:
     *                type: string
     *              generatedFileName:
     *                type: string
     *              taskId:
     *                type: string
     *              processVersionId:
     *                type: string
     *              reportTemplateVariables:
     *                type: object
     *     responses:
     *       200:
     *         description: protocol number
     *         schema:
     *           $ref: '#/definitions/ServiceResponseGeneratePdf'
     *       500:
     *         description: Generic Error
     *         schema:
     *           $ref: '#/definitions/ServiceResponseError'
     */
    public generatePdfForProcess(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const { userRegistryId, processInstanceId, reportTemplateRef, generatedFileName, taskId, processVersionId, reportTemplateVariables, istanzaOperatore } = request.payload;
        const ipa = request.params['ipa'];
        const filename = `${generatedFileName}.pdf`;
        const fieldName = `pdf_file_${taskId}`;
        let userDocument: Model.UserContent;

        this.logger.info('Generating process report', {
            userRegistryId,
            ipa,
            filename,
            reportTemplateRef,
            generatedFileName,
            taskId,
            processVersionId,
            reportTemplateVariables,
            istanzaOperatore
        });

        this.fileManagementService.getProcessInstanceFiles(userRegistryId, ipa, processInstanceId, istanzaOperatore)
            .then(userFile => {
                if (!userFile)
                    throw new ServiceError(`Cannot find user documents for process instance id = ${processInstanceId}`);

                this.logger.info('User documents have been retrieved. Generating report', {
                    userRegistryId,
                    ipa,
                    filename,
                    reportTemplateRef,
                    generatedFileName,
                    taskId,
                    processVersionId,
                    reportTemplateVariables,
                    istanzaOperatore
                });

                return Q.all([
                    this.userDocsUtils.storePdfReport(ipa, reportTemplateRef, null, userFile.userId, Object.keys(reportTemplateVariables).reduce((p, c) => {
                        const attachment = userFile.contents.find(a => a.fieldName === c);

                        if (attachment) {
                            p[c] = attachment.file;
                        } else {
                            p[c] = reportTemplateVariables[c];
                        }

                        return p;
                    }, {}), filename),
                    this.userDocsUtils.isUserDocumentVisible(ipa, processVersionId, fieldName)
                ]);
            })
            .then(results => {
                this.logger.info('Process report has been generated. Updating file', {
                    userRegistryId,
                    istanzaOperatore,
                    ipa,
                    filename,
                    fieldName
                });

                const [pdf, isDocumentVisible] = results;
                userDocument = FileManagementService.getUserContent(
                    ipa,
                    filename,
                    pdf.id,
                    pdf.mimeType,
                    pdf.length,
                    isDocumentVisible,
                    filename
                );

                return this.fileManagementService.updateFile(userRegistryId, processInstanceId, ipa,
                    { reportPdfFile: userDocument.file.id }, null, [userDocument], null, null, istanzaOperatore);
            })
            .then(() => {
                this.logger.info('File has been successfully updated', {
                    userRegistryId,
                    istanzaOperatore,
                    ipa,
                    filename,
                    fieldName
                });

                reply.replyWithEntity({ reportPdfFile: userDocument.file.id });
            })
            .catch(error => {
                this.logger.error('An error occurred while generating process report', {
                    userRegistryId,
                    istanzaOperatore,
                    ipa,
                    filename,
                    fieldName,
                    error: error ? error.message : null,
                    stack: error ? error.stack : null
                });

                reply.replyWithError(error);
            });
    }

    public saveFile(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const ipa = request.params['ipa'];

        const userRegistryId: string = request.payload.userRegistryId;
        const processInstanceId: string = request.payload.processInstanceId;
        const content: string = request.payload.content[Object.keys(request.payload.content)[0]];
        const mimeType: string = request.payload.fileMimeType;
        const fileName: string = request.payload.fileName;
        const isVisibleToUser: boolean = request.payload.isVisibleToUser;

        let userDocument = null;

        // Create a stream for file storage
        const Duplex = require('stream').Duplex;

        const buffer = new Buffer(content, 'base64');
        const stream = new Duplex();

        stream.length = buffer.byteLength;
        stream.push(buffer);
        stream.push(null);
        let istanzaOperatore : boolean = false
        this.getUserRegistryId(processInstanceId).then((id: RegistryId) => {                
            istanzaOperatore = id.istanzaOperatore;
            return this.fileManagementService.getProcessInstanceFiles(userRegistryId, ipa, processInstanceId, istanzaOperatore)
            })
            .then(userFile => {
                if (!userFile)
                    throw new ServiceError(`Cannot find user documents for process instance id = ${processInstanceId}`);

                return this.userDocsUtils.storeFile(
                    stream,
                    userFile.userId,
                    fileName,
                    mimeType
                );
            })
            .then(result => {
                userDocument = FileManagementService.getUserContent(
                    ipa,
                    fileName,
                    result.id,
                    mimeType,
                    result.length,
                    isVisibleToUser,
                    fileName
                );

                return this.fileManagementService.updateFile(
                    userRegistryId,
                    processInstanceId,
                    ipa, {
                        reportPdfFile: userDocument.file.id
                    }, null, [userDocument]);
            })
            .then(() => {
                reply.replyWithEntity({
                    fileName,
                    userDocumentRegistryId: userDocument.id,
                    id: userDocument.file.id
                });
            })
            .catch(error => {
                reply.replyWithError(error);
            });
    }

    public bookEvent(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const ipa = request.params['ipa'];

        const resourceId: string = request.payload.resource[Object.keys(request.payload.resource)[0]];
        const title: string = request.payload.title[Object.keys(request.payload.title)[0]];
        let slots: Array<Model.CalendarSlot>;
        try {
            slots = JSON.parse(request.payload.slots[Object.keys(request.payload.slots)[0]]).map(s => {
                return {
                    startTime: s.startTime,
                    endTime: s.endTime
                }
            });
        } catch (e) {
            slots = request.payload.slots[Object.keys(request.payload.slots)[0]].map(s => {
                return {
                    startTime: s.startTime,
                    endTime: s.endTime
                }
            });
        }
        const fiscalCode: string = request.payload.fiscalCode ? request.payload.fiscalCode[Object.keys(request.payload.fiscalCode)[0]] : undefined
        const practice: string = request.payload.practice ? request.payload.practice[Object.keys(request.payload.practice)[0]] : undefined

        let events = null;
        let resource = null;
        let typology = null;

        const eventToInsert = this.calendarUtils.getContiguousSlots(slots);

        this.verifyMultiEventScheduling(
            resourceId,
            ipa,
            eventToInsert
        )
            .then(result => {
                if (result === null || result.ipa != ipa)
                    throw new ServiceError('Resource not found');

                resource = result
                return this.typologyRepository.findById(resource.typology)
            })
            .then(result => {
                if (result === null || result.ipa != ipa)
                    throw new ServiceError('Typology not found');
                typology = result;

                events = []
                eventToInsert.forEach(s => {
                    const toInsert = <any>{
                        resource: resource.id,
                        title: title,
                        startTime: s.startTime,
                        endTime: s.endTime,
                        status: resource.confimationEnabled ? PENDING_EVENT : CONFIRMED_EVENT,
                        extension: [],
                        createdAt: new Date()
                    }
                    if (fiscalCode) {
                        toInsert.extension.push({
                            key: 'Codice fiscale',
                            type: 'string',
                            value: fiscalCode,
                            isVisible: false
                        })
                    }
                    if (practice) {
                        toInsert.extension.push({
                            key: 'Numero pratica',
                            type: 'string',
                            value: practice,
                            isVisible: false
                        })
                    }

                    events.push(toInsert);
                })

                // Massive insert
                return this.eventRepository.bulkInsert(events)
            })
            .then((results) => {
                let ids = []
                results.forEach(event => {
                    ids.push(event.id.toString())
                    this.messagePusher.push(<MyTransferModel.CalendarEventTransferableMessage>{
                        type: 'calendar_event_inserted',
                        origin: 'myintranet',
                        payload: {
                            type: 'calendar_event_inserted',
                            ipa: resource.ipa,
                            data: {
                                id: event.id,
                                title: event.title,
                                startTime: event.startTime,
                                endTime: event.endTime,
                                status: event.status,
                                extension: event.extension ? event.extension
                                    .filter(e => e.isVisible)
                                    .map(o => ({
                                        key: o.key,
                                        type: o.type,
                                        value: o.value,
                                        isVisible: o.isVisible
                                    })) : [],
                                createdAt: event.createdAt,
                                resource: {
                                    id: resource.id.toString(),
                                    typology: {
                                        id: typology.id.toString(),
                                        ipa: typology.ipa,
                                        name: typology.name,
                                        description: typology.description,
                                        isPublic: typology.isPublic,
                                        createdAt: typology.createdAt
                                    },
                                    instanceFormId: resource.instanceFormId,
                                    ipa: resource.ipa,
                                    name: resource.name,
                                    description: resource.description,
                                    isPublic: resource.isPublic,
                                    slotUnit: resource.slotUnit,
                                    daysBeforeDel: resource.daysBeforeDel,
                                    daysCanBook: resource.daysCanBook,
                                    maxBook: resource.maxBook,
                                    hours: resource.hours ? resource.hours.map(o => ({
                                        day: o.day,
                                        hourStart: o.hourStart,
                                        hourEnd: o.hourEnd
                                    })) : [],
                                    maxSlotToView: resource.maxSlotToView,
                                    confimationEnabled: resource.confimationEnabled,
                                    enabledDateStart: resource.enabledDateStart,
                                    enabledDateEnd: resource.enabledDateEnd,
                                    labelColor: resource.labelColor,
                                    createdAt: resource.createdAt
                                }
                            }
                        }
                    });
                });
                return ids;
            })
            .then((ids) => {
                reply.replyWithEntity({ eventIds: ids.join(',') });
            })
            .catch(error => {
                reply.replyWithError(error);
            });
    }

    public confirmEvent(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const ipa = request.params['ipa'];
        const eventIds: string = request.payload.eventIds[Object.keys(request.payload.eventIds)[0]];
        const confirmed: boolean = request.payload.confirmed[Object.keys(request.payload.confirmed)[0]];
        const ids = eventIds.split(',');
        const newStatus = confirmed ? CONFIRMED_EVENT : DECLINE_EVENT;
        let events = null;
        let resource = null;
        let typology = null;

        this.eventRepository.findByIds(ids)
            .then(results => {
                if (results === null || results.length !== ids.length)
                    throw new ServiceError('Events not found');

                events = results;
                let auxRes = events[0].resource;
                for (let i = 1; i < events.length; i++) {
                    const element = events[i];
                    if (element.resource.toString() != auxRes.toString())
                        throw new ServiceError('Events have different resources');
                }

                return this.resourceRepository.findById(auxRes);
            })
            .then(result => {
                if (result === null || result.ipa != ipa)
                    throw new ServiceError('Resource not found');

                resource = result;

                return this.typologyRepository.findById(resource.typology)
            })
            .then(result => {
                if (result === null || result.ipa != ipa)
                    throw new ServiceError('Typology not found');

                typology = result;

                return this.eventRepository.updateStatus(ids, newStatus)
            })
            .then(() => {
                events.forEach(event => {
                    this.messagePusher.push(<MyTransferModel.CalendarEventTransferableMessage>{
                        type: 'calendar_event_updated',
                        origin: 'myintranet',
                        payload: {
                            type: 'calendar_event_updated',
                            ipa: resource.ipa,
                            data: {
                                id: event.id,
                                title: event.title,
                                startTime: event.startTime,
                                endTime: event.endTime,
                                status: newStatus,
                                extension: event.extension ? event.extension
                                    .filter(e => e.isVisible)
                                    .map(o => ({
                                        key: o.key,
                                        type: o.type,
                                        value: o.value,
                                        isVisible: o.isVisible
                                    })) : [],
                                createdAt: event.createdAt,
                                resource: {
                                    id: resource.id.toString(),
                                    typology: {
                                        id: typology.id.toString(),
                                        ipa: typology.ipa,
                                        name: typology.name,
                                        description: typology.description,
                                        isPublic: typology.isPublic,
                                        createdAt: typology.createdAt
                                    },
                                    instanceFormId: resource.instanceFormId,
                                    ipa: resource.ipa,
                                    name: resource.name,
                                    description: resource.description,
                                    isPublic: resource.isPublic,
                                    slotUnit: resource.slotUnit,
                                    daysBeforeDel: resource.daysBeforeDel,
                                    daysCanBook: resource.daysCanBook,
                                    maxBook: resource.maxBook,
                                    hours: resource.hours ? resource.hours.map(o => ({
                                        day: o.day,
                                        hourStart: o.hourStart,
                                        hourEnd: o.hourEnd
                                    })) : [],
                                    maxSlotToView: resource.maxSlotToView,
                                    confimationEnabled: resource.confimationEnabled,
                                    enabledDateStart: resource.enabledDateStart,
                                    enabledDateEnd: resource.enabledDateEnd,
                                    labelColor: resource.labelColor,
                                    createdAt: resource.createdAt
                                }
                            }
                        }
                    });
                });
            })
            .then(() => {
                reply.replyWithJustSuccessState();
            })
            .catch(error => {
                reply.replyWithError(error);
            });
    }

    public deleteEvent(request: SessionAwareRequest, reply: IAugmentedEntityReply): void {
        const ipa = request.params['ipa'];
        const eventIds: string = request.payload.eventIds[Object.keys(request.payload.eventIds)[0]];
        const ids = eventIds.split(',');
        let events = null
        this.eventRepository.findByIds(ids)
            .then(results => {
                if (results === null || results.length != ids.length)
                    throw new ServiceError('Events not found');

                events = results;
                let auxRes = events[0].resource;
                for (let i = 1; i < events.length; i++) {
                    const element = events[i];
                    if (element.resource.toString() != auxRes.toString())
                        throw new ServiceError('Events have different resources');
                }

                return this.resourceRepository.findById(auxRes);
            })
            .then(resource => {
                if (resource === null)
                    throw new ServiceError('resource not found');

                events.forEach(event => {
                    let dayBef = resource.daysBeforeDel ? resource.daysBeforeDel : 0
                    const dateToComapre = this.calendarUtils.subtractDays(dayBef, event.startTime)

                    if ((new Date()).getTime() > dateToComapre.getTime()) {
                        throw new ServiceError('Error on day before delete');
                    }
                });

                return this.eventRepository.removeMany(ids);
            })
            .then(() => {
                events.forEach(event => {
                    this.messagePusher.push(<MyTransferModel.CalendarEventDeleteTransferableMessage>{
                        type: 'calendar_event_deleted',
                        origin: 'myintranet',
                        payload: {
                            type: 'calendar_event_deleted',
                            ipa: ipa,
                            data: {
                                id: event.id,
                                ipa: ipa
                            }
                        }
                    });
                });
            })
            .then(() => {
                reply.replyWithJustSuccessState();
            })
            .catch(error => {
                reply.replyWithError(error);
            });
    }

    /**
     * Verify if a list of slots time is bookable
     */
    private verifyMultiEventScheduling(resourceId: string, ipa: string, slots: Array<Model.CalendarSlot>, ingoreEvent?: string): Q.Promise<ResourceDocument> {
        let from = slots[0].startTime;
        let to = slots[slots.length - 1].endTime;
        return Q.Promise<ResourceDocument>((resolve, reject) => {
            this.eventRepository.findAll(from, to, [resourceId], [], ingoreEvent, true)
                .then(events => {
                    if (events.length > 0) {
                        // verifica che i vari slot non interfericano gli attuali
                        events.forEach(event => {
                            let diff = []
                            diff = slots.filter(s => {
                                return (s.startTime >= event.startTime && s.startTime < event.endTime)
                                    || (s.endTime > event.startTime && s.endTime <= event.endTime)
                            })
                            if (diff.length > 0)
                                throw new ServiceError('Already exist events in this range')
                        })
                    }

                    return this.resourceRepository.findByEnabled(resourceId, ipa, from, to)
                })
                .then(resource => {
                    if (resource === null)
                        throw new ServiceError('Resource not found');

                    if (resource.enabledDateStart && from < resource.enabledDateStart) {
                        throw new ServiceError('Resource not enabled in this date');
                    }
                    if (resource.enabledDateEnd && to > resource.enabledDateEnd) {
                        throw new ServiceError('Resource not enabled in this date');
                    }

                    if (from < this.calendarUtils.addDays(resource.daysCanBook).getTime())
                        throw new ServiceError('Too late to book in this dates');

                    let nSlots = 0;
                    slots.forEach(s => {
                        nSlots += this.calendarUtils.numSlotsInRange(s.startTime, s.endTime, resource)
                    })

                    if (nSlots > resource.maxBook)
                        throw new ServiceError('Try to book too much slots');

                    resolve(resource)
                })
                .catch(e => { reject(e) })

        })
    }

    private changeCitizenVisibilityDocuments(request: SessionAwareRequest, reply: IAugmentedEntityReply) {
        const keys = Object.keys(request.payload.fileVariables);
        let istanzaOperatore : boolean = false
        return this.getUserRegistryId(request.payload.processInstanceId)
        .then((id: RegistryId) => {
            
            istanzaOperatore = id.istanzaOperatore;
        return this.fileManagementService
            .getProcessInstanceFiles(request.payload.userRegistryId, request.params['ipa'], request.payload.processInstanceId, istanzaOperatore)
            .then(result => {
                const attachments = result.contents.map(content => {
                    if (keys.indexOf(content.fieldName) !== -1) {
                        content.isUserVisible = request.payload.visibilityValue;
                        return content
                    } else
                        return content;
                })
                return this.fileManagementService.updateFile(request.payload.userRegistryId, request.payload.processInstanceId, request.params['ipa'], {}, undefined, attachments)
            })
            .then((result) => {
                reply.replyWithEntity(result)
            })
        })
    }

    //tutti i service task con   this.fileManagementService.getProcessInstanceFiles devono essere adattati 

    private sendMail(request: SessionAwareRequest, reply: IAugmentedEntityReply) {
        let {userRegistryId, processInstanceId, mailRecipients, subject, body, type, formsVariables} = request.payload;
        const keys = request.payload.fileVariables ? request.payload.fileVariables.split(',') : [];
        let filesData;
        const recipients = mailRecipients.split(',').map(recipient => recipient.trim());
        this.logger.info('Getting process instance variables and user files', {
            processInstanceId
        });
        let istanzaOperatore : boolean = false
        return this.getUserRegistryId(processInstanceId)
            .then((id: RegistryId) => {
                
                istanzaOperatore = id.istanzaOperatore;

                return Q.all([this.bpmEngineClient
                    .getProcessInstanceVariables(processInstanceId),
                this.fileManagementService
                    .getProcessInstanceFiles(userRegistryId, request.params['ipa'], processInstanceId, istanzaOperatore)])
                    .then(([variables, file]) => {
                        this.logger.info('instance variables and user files retrived');
                        this.logger.info('variable replace for subject started');
                        subject = this.replacePlaceholderWithVariables(subject, variables, formsVariables);
                        this.logger.info('variable replace for subject ended');
                        this.logger.info('variable replace for body started');
                        body = this.replacePlaceholderWithVariables(body, variables, formsVariables);
                        this.logger.info('variable replace for body ended');
                        filesData = file.contents.filter(file => keys.indexOf(file.fieldName) !== -1)
                        this.logger.info('retrived desired files')
                        this.logger.info(filesData);
                        return Q.all(filesData.map(fileDesired => {
                            return this.boxRepository.get(file.userId, fileDesired.file.id)
                        }                           
                        ));
                    })
                    .then((files) => {
                        this.logger.info('files retrived')
                        const attachments: Array<ContentShareableAttribute> = files.map((file, index) => (<ContentShareableAttribute>{
                            name: `Content-Disposition: attachment; filename="${filesData[index].name}"`,
                            value: file['read'] ? file['read']().toString('base64') : null,
                            mimeType: filesData[index].file.mimeType,
                            isBase64Encoded: true
                        }));
                        this.logger.info('attachments composed, sending to recievers')
                        return this.sendToRecieversInSequence(recipients, [], processInstanceId, request.params['ipa'], type, subject, body, attachments);
                    }).then(() => {
                        reply.replyWithJustSuccessState();
                    }).catch(error => {
                        this.logger.error(error);
                        reply.replyWithError(error)
                    })
            })
            .catch(error => {
                this.logger.error(error);
                reply.replyWithError(error)
            })
    }

    private replacePlaceholderWithVariables(text: string, variablesMap: { [key: string]: Model.VariableValue<any> }, formsVariables: { [key: string]: Model.VariableValue<any> }) {
        if (text.match(/\${{.*?}}/g)) {
            const variablesToReplace = text.match(/\${{.*?}}/g).map(s => s.replace("${{", '').replace("}}", "")); //trim is not possible because we can't reconstruct the original number of spaces
            if (variablesToReplace)
                variablesToReplace.forEach(variable => {
                    let newVariable :any;
                    if(formsVariables && formsVariables[variable]) newVariable = formsVariables[variable];
                    else if(variablesMap[variable] && variablesMap[variable].value) newVariable = variablesMap[variable].value.toString();
                    else newVariable = '';
                    text = text.replace("${{" + variable + "}}", newVariable);                
                })
        }
        return text;
    }

    private getStatusUpdateNotificationContents(ipa: string, variables: { [key: string]: Model.VariableValue<any> },
        profile: ISessionUser): Q.Promise<Array<ContentShareableAttribute>> {
        this.logger.info('Retrieving instance status update contents from MyConfig');

        return this.configHelper
            .getItem(`${ipa}>myp3>istancestatusupdatecontents`)
            .then(results => {
                this.logger.info('Instance status update contents have been retrieved from MyConfig');

                return results.value.map(r => {
                    const values = r.split(':').filter(r => r);

                    return <ContentShareableAttribute>{
                        name: values[0],
                        value: mustache.render(decodeURIComponent(values[1]), {
                            variables,
                            profile
                        }),
                        mimeType: 'text/html'
                    };
                });
            });
    }

    /**
     * 
     * 
     * @author Giulio Montagner
     * @private
     * @param {string} processInstanceId 
     * @returns {Q.Promise<string>} 
     * @memberof ServiceTasksService
     */
    private getUserRegistryId(processInstanceId: string): Q.Promise<RegistryId> {
        return this.bpmEngineClient
            .getProcessInstanceVariables(processInstanceId)
            .then(variables => {
                if (!variables['userRegistryId'] || !variables['userRegistryId'].value)
                    throw new ServiceError('Cannot retrive user registry id');
                const istanzaOperatore: boolean = (variables['istanzaOperatore'] && variables['istanzaOperatore'].value == true);
                return {
                    registryId: variables['userRegistryId'].value,
                    istanzaOperatore
                }
            });
    }

    /**
     * @author Angelo Castaldo
     * 
     * @param {SessionAwareRequest} request 
     * @param {IAugmentedEntityReply} reply 
     * 
     * @memberof ServiceTasksService
     * 
     * @swagger
     * definitions:
     *   ServiceResponseService1:
     *     allOf:
     *     - $ref: '#/definitions/ServiceResponse'
     *     - type: object
     *       required:
     *       - entity
     *       properties:
     *         entity:
     *           type: object
     *           required:
     *           - message
     *           properties:
     *             message:
     *               type: string
     *           
     *           
     * 
     * /myintranet/{ipa}/instances/service-task/service1:
     *   post:
     *     tags:
     *     - 'ServiceTasks Service1'
     *     description: Get Example Service.
     *     produces:
     *       - application/json
     *     parameters:
     *       - name: ipa
     *         description: Portal's IPA.
     *         in: path
     *         required: true
     *         type: string
     *       - name: payload
     *         description: get message with name and surname 
     *         in: body
     *         required: true
     *         schema:
     *            type: object
     *            required:
     *            - name
     *            - surname
     *            properties:
     *              name:
     *                type: string
     *              surname:
     *                type: string
     *     responses:
     *       200:
     *         description: message di benvenuto
     *         schema:
     *           $ref: '#/definitions/ServiceResponseService1'
     *       500:
     *         description: Generic Error
     *         schema:
     *           $ref: '#/definitions/ServiceResponseError'
     */
    public getMessage(request, response) {
        var nome = request.payload.name;
        var cognome = request.payload.surname;
        // var eta = request.payload.age;
        // var dataNascita= request.payload.dateBirth;
        var message = "Benvenuto \b" + nome + "\b" + cognome;
        //response.replyWithJustSuccessState();
        return response(message);
    }

    /**
     *
     *
     * @private
     * @param {stream.Readable} stream
     * @returns {Q.Promise<Buffer>}
     * @memberof ServiceTasksService
     */
    private toBuffer(stream: stream.Readable): Q.Promise<Buffer> {
        return Q.Promise<Buffer>((resolve, reject) => {
            const data = [];

            stream.on('data', d => {
                data.push(d);
            });

            stream.on('error', (error) => {
                reject(error);
            });

            stream.on('end', () => {
                resolve(Buffer.concat(data));
            });
        });
    }


    private sendToRecieversInSequence(array: Array<string>, acc: any[] = [], processInstanceId: string, ipa: string, type: Channel, subject: string, body: string, attachments: Array<ContentShareableAttribute>) {
        if (array && array.length) {
            const recipient = array.pop();
            return this.communicationSender.sendCommunicationToReceiver(
                processInstanceId,
                'Mail from service task',
                'service-task-mail',
                ipa,
                type,
                '',
                '',
                recipient,
                [{
                    mimeType: 'text/html',
                    name: 'default-subject',
                    value: subject,
                    isBase64Encoded: false
                }, {
                    mimeType: 'text/html',
                    isBase64Encoded: false,
                    name: 'default-body',
                    value: body
                }].concat(attachments))
                .then((resolved) => {
                    return this.sendToRecieversInSequence(array, acc.concat(resolved), processInstanceId, ipa, type, subject, body, attachments)
                })
        }
        else
            return Q.resolve(acc)
    }
}
