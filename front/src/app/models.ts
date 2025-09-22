
export interface Uploading {
  file:File
  status: '' | 'pending' | 'uploading' | 'success' | 'error';
  progression:number ;
}

export interface Fichier {
  fileName: string;
  path: string;
  filePath:String;
}

export interface DocumentApp {
  id: string;
  title: string;
  description?: string;
  fichiers: Fichier[];
}
